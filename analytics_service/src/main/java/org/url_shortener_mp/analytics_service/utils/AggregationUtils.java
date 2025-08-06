package org.url_shortener_mp.analytics_service.utils;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AggregationUtils {
    public static AggregationOperation matchOperation(List<MatchParameters<?>> parameters) {
        Criteria criteria = new Criteria();

        List<Criteria> criteriaList = new ArrayList<>();

        for (MatchParameters<?> param : parameters) {
            Criteria query = Criteria.where(param.getName());
            query = param.getCriteriaComparator().compare(query, param.getValue(), param.getName());
            criteriaList.add(query);
        }

        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        return Aggregation.match(criteria);
    }

    public static AggregationExpression mergeMapsExpression(String fieldName) {
        return context -> new Document("$reduce", new Document()
                .append("input", "$" + fieldName)       // Array of maps (e.g., agentCounts)
                .append("initialValue", new Document()) // Start with empty map
                .append("in", new Document("$concatArrays", new Document(
                        "$$value", new Document("$objectToArray", "$$this")


                )
                ))
        );
    }

    @SuppressWarnings("unchecked")
    public static List<Document> createMapFacet(String sourceField, String outputField) {
        return Arrays.asList(
                new Document("$unwind", "$" + sourceField),
                new Document("$project", new Document("kv", new Document("$objectToArray", "$" + sourceField))),
                new Document("$unwind", "$kv"),
                new Document("$group", new Document("_id", "$kv.k").append("v", new Document("$sum", "$kv.v"))),
                new Document("$group", new Document("_id", null).append(outputField,
                        new Document("$push", new Document("k", "$_id").append("v", "$v")))),
                new Document("$project", new Document(outputField, new Document("$arrayToObject", "$" + outputField)))
        );
    }

    public static AggregationExpression mergeAndSumMap(String fieldName) {
        return context -> new Document("$arrayToObject",
                new Document("$map",
                        new Document("input",
                                new Document("$reduce",
                                        new Document("input", "$" + fieldName)
                                                .append("initialValue", Collections.emptyList())
                                                .append("in", new Document("$concatArrays", Arrays.asList(
                                                        "$$value",
                                                        new Document("$objectToArray", "$$this")
                                                )))
                                )
                        )
                                .append("as", "item")
                                .append("in", new Document("k", "$$item.k")
                                        .append("v", new Document("$sum", Arrays.asList("$$item.v"))))
                ));
    }
//    public static AggregationOperation groupOperation(String key,)

}
