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


//    public static AggregationOperation groupOperation(String key,)

}
