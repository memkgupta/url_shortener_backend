package org.url_shortener_mp.analytics_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.url_shortener_mp.analytics_service.dtos.*;

import org.url_shortener_mp.analytics_service.entities.Entry;
import org.url_shortener_mp.analytics_service.repositories.EntryRepository;
import org.url_shortener_mp.analytics_service.utils.AggregationUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static org.url_shortener_mp.analytics_service.utils.AggregationUtils.*;

@Service
public class AnalyticService {
    private final EntryRepository entryRepository;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AnalyticService(EntryRepository entryRepository, MongoTemplate mongoTemplate) {
        this.entryRepository = entryRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void writeLog(WindowedAnalytic window) {
        try {

            Entry entry = new Entry();
            entry.setClicks(window.getTotalClicks());
            entry.setEndTime(
                    window.getWindowEnd()
            );
            entry.setStartTime(
                    window.getWindowStart()
            );
            entry.setTimestamp(new Timestamp(System.currentTimeMillis()));
            entry.setUrl(window.getShortUrl());
            entry.setAgentMap(window.getUserAgentCounts());
            entry.setGeoMap(new HashMap<>());
            entry.setCountryMap(window.getCountryCounts());
            entry.setPlatformMap(window.getPlatformCounts());
            entry.setDeviceMap(window.getDeviceCounts());
            entry.setOsMap(window.getOsCounts());
            entry.setBrowserMap(window.getBrowserCounts());
            entryRepository.save(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Cacheable(value = "analytics-clicks-over-time", key = "#params.urlId+':'+#params.startTime.toString()+ ':' + #params.endTime.toString()", unless = "#result == null")
    public ClicksOverTime getCLicksOverTime(QueryParameters params) {
        String url_id = params.getUrlId();
        Timestamp startTime = params.getStartTime();
        Timestamp endTime = params.getEndTime();

        String windowUnit = params.getWindowUnits().getMongoUnit();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("url").is(url_id).andOperator(
                                Criteria.where("startTime").gte(startTime).andOperator(
                                        Criteria.where("endTime").lte(endTime)
                                )
                        )
                ),
                Aggregation.project("clicks")
                        .andExpression("{" +
                                "$dateTrunc:{date:\"$startTime\", unit: \"" + windowUnit + "\" }" + "}")
                        .as("interval"),
                Aggregation.group("interval").sum("clicks").as("totalClicks"),
                Aggregation.sort(Sort.Direction.ASC, "_id")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "entry", Map.class
        );

//        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "entry", Map.class);
        List<Map> mappedResults = results.getMappedResults();
        ClicksOverTime response = mapClicksOverTime(mappedResults, params.getWindowUnits().name());
        return response;

    }

    public ClicksOverTime mapClicksOverTime(List<Map> aggregationResults, String timeUnit) {
        // Convert aggregation results to List<TimelyClick>
        List<TimelyClick> clicks = aggregationResults.stream()
                .map(result -> {
                    String time = result.get("_id").toString(); // interval
                    int clicksCount = Integer.parseInt(result.get("totalClicks").toString());

                    TimelyClick timelyClick = new TimelyClick();
                    timelyClick.setTime(time);
                    timelyClick.setClicks(clicksCount);

                    // Optional uniqueClicks (if present in aggregation)
                    if (result.containsKey("uniqueClicks")) {
                        timelyClick.setUniqueClicks(
                                Integer.parseInt(result.get("uniqueClicks").toString())
                        );
                    }

                    return timelyClick;
                })
                .collect(Collectors.toList());

        // Build ClicksOverTime response
        ClicksOverTime clicksOverTime = new ClicksOverTime();
        clicksOverTime.setLength(clicks.size());
        clicksOverTime.setClicks(clicks);
        clicksOverTime.setTimeUnit(TimeUnit.valueOf(timeUnit.toUpperCase())); // Convert to enum

        return clicksOverTime;
    }

    @Cacheable(value = "analytics-hourly-click", key = "#params.urlId+':'+#params.startTime.toString()+ ':' + #params.endTime.toString()", unless = "#result == null")
    public List<HourlyClick> getHourlyClicks(QueryParameters params) {
        String urlId = params.getUrlId();
        Timestamp startTime = params.getStartTime();
        Timestamp endTime = params.getEndTime();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("url").is(urlId).andOperator(
                                Criteria.where("startTime").gte(startTime).andOperator(
                                        Criteria.where("endTime").lte(endTime)
                                )
                        )
                ),
                Aggregation.project().andExpression("hour(timestamp)").as("hour")
                        .and("clicks").as("clicks"),
                Aggregation.group("hour")
                        .sum("clicks").as("totalClicks"),
                Aggregation.sort(Sort.Direction.ASC, "_id")
        );
        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "entry", Map.class
        );
        List<Map> mappedResults = results.getMappedResults();
        return mapToHourlyClicks(mappedResults);
    }

    public List<HourlyClick> mapToHourlyClicks(List<Map> aggregationResults) {
        List<HourlyClick> hourlyClicks = new ArrayList<>();
        aggregationResults.stream()
                .forEach(result -> {
                    String time = String.format("%02d:00", Integer.parseInt(result.get("_id").toString()));
                    long clicks = Long.parseLong(result.get("totalClicks").toString());
                    HourlyClick hourlyClick = new HourlyClick();
                    hourlyClick.setHour(time);
                    hourlyClick.setClicks(clicks);
                    hourlyClicks.add(hourlyClick);
                });
        return hourlyClicks;
    }

    public List<Map> getDevicesOverTime(QueryParameters params) {
        String url_id = params.getUrlId();
        Timestamp startTime = params.getStartTime();
        Timestamp endTime = params.getEndTime();

        String windowUnit = params.getWindowUnits().getMongoUnit();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("url").is(url_id).andOperator(
                                Criteria.where("startTime").gte(startTime).andOperator(
                                        Criteria.where("endTime").lte(endTime)
                                )
                        )
                ),
                Aggregation.project()
                        .andExpression("{$objectToArray: \"$deviceMap\"}")
                        .as("deviceArray"),
                Aggregation.unwind("deviceArray"),
                Aggregation.group("deviceArray.k")
                        .sum("deviceArray.v").as("totalClicks"),

                Aggregation.sort(Sort.Direction.ASC, "totalClicks")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "entry", Map.class
        );

        return results.getMappedResults();
    }

    @Cacheable(value = "analytics-metrics-os", key = "#params.urlId+':'+#params.startTime.toString()+ ':' + #params.endTime.toString()", unless = "#result == null")
    public List<Map> getOperatingSystemsOverTime(QueryParameters params) {
        String url_id = params.getUrlId();
        Timestamp startTime = params.getStartTime();
        Timestamp endTime = params.getEndTime();

        String windowUnit = params.getWindowUnits().getMongoUnit();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("url").is(url_id).andOperator(
                                Criteria.where("startTime").gte(startTime).andOperator(
                                        Criteria.where("endTime").lte(endTime)
                                )
                        )
                ),
                Aggregation.project()
                        .andExpression("{$objectToArray: \"$osMap\"}")
                        .as("osArray"),
                Aggregation.unwind("osArray"),
                Aggregation.group("osArray.k")
                        .sum("osArray.v").as("totalClicks"),

                Aggregation.sort(Sort.Direction.ASC, "totalClicks")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "entry", Map.class
        );

        return results.getMappedResults();
    }

    @Cacheable(value = "analytics-metrics-browsers", key = "#params.urlId+':'+#params.startTime.toString()+ ':' + #params.endTime.toString()", unless = "#result == null")
    public List<Map> getBrowsersOverTime(QueryParameters params) {
        String url_id = params.getUrlId();
        Timestamp startTime = params.getStartTime();
        Timestamp endTime = params.getEndTime();

        String windowUnit = params.getWindowUnits().getMongoUnit();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("url").is(url_id).andOperator(
                                Criteria.where("startTime").gte(startTime).andOperator(
                                        Criteria.where("endTime").lte(endTime)
                                )
                        )
                ),
                Aggregation.project()
                        .andExpression("{$objectToArray: \"$browserMap\"}")
                        .as("browserArray"),
                Aggregation.unwind("browserArray"),
                Aggregation.group("browserArray.k")
                        .sum("browserArray.v").as("totalClicks"),

                Aggregation.sort(Sort.Direction.ASC, "totalClicks")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "entry", Map.class
        );

        return results.getMappedResults();
    }

    @Cacheable(value = "analytics-metrics-referrers", key = "#params.urlId+':'+#params.startTime.toString()+ ':' + #params.endTime.toString()", unless = "#result == null")
    public List<Map> getReferrersOverTime(QueryParameters params) {
        String url_id = params.getUrlId();
        Timestamp startTime = params.getStartTime();
        Timestamp endTime = params.getEndTime();

        String windowUnit = params.getWindowUnits().getMongoUnit();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("url").is(url_id).andOperator(
                                Criteria.where("startTime").gte(startTime).andOperator(
                                        Criteria.where("endTime").lte(endTime)
                                )
                        )
                ),
                Aggregation.project()
                        .andExpression("{$objectToArray: \"$referrerMap\"}")
                        .as("referrerArray"),
                Aggregation.unwind("referrerArray"),
                Aggregation.group("referrerArray.k")
                        .sum("referrerArray.v").as("totalClicks"),

                Aggregation.sort(Sort.Direction.ASC, "totalClicks")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "entry", Map.class
        );

        return results.getMappedResults();
    }

    @Cacheable(value = "analytics-overviee", key = "#urlId+':'+#startTime.toString()+ ':' + #endTime.toString()", unless = "#result == null")
    public DashboardPayload getOverview(String urlId, Timestamp startTime, Timestamp endTime) {
        try {
            // Build the aggregation pipeline

            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("url").is(urlId).andOperator(
                            Criteria.where("startTime").gte(startTime).andOperator(
                                    Criteria.where("endTime").lte(endTime)
                            )
                    )),
                    Aggregation.group().sum("clicks").as("totalClicks").push("agentMap")
                            .as("agentCounts")
                            .push("browserMap")
                            .as("browserCounts")
                            .push("osMap")
                            .as("osCounts")
                            .push("deviceMap")
                            .as("deviceCounts")
                            .push("countryMap")
                            .as("countryCounts"),
                    context -> new Document("$facet", new Document()
                            .append("metadata", Arrays.asList(
                                    new Document("$project", new Document("totalClicks", 1))
                            ))
                            .append("agentMap", createMapFacet("agentCounts", "agentMap"))
                            .append("browserMap", createMapFacet("browserCounts", "browserMap"))
                            .append("osMap", createMapFacet("osCounts", "osMap"))
                            .append("deviceMap", createMapFacet("deviceCounts", "deviceMap"))
                            .append("countryMap", createMapFacet("countryCounts", "countryMap"))
                    ),
                    Aggregation.project()
                            .and(context -> new Document("$arrayElemAt", Arrays.asList("$metadata.totalClicks", 0))).as("totalClicks")
                            .and(context -> new Document("$arrayElemAt", Arrays.asList("$agentMap.agentMap", 0))).as("agentMap")
                            .and(context -> new Document("$arrayElemAt", Arrays.asList("$browserMap.browserMap", 0))).as("browserMap")
                            .and(context -> new Document("$arrayElemAt", Arrays.asList("$osMap.osMap", 0))).as("osMap")
                            .and(context -> new Document("$arrayElemAt", Arrays.asList("$deviceMap.deviceMap", 0))).as("deviceMap")
                            .and(context -> new Document("$arrayElemAt", Arrays.asList("$countryMap.countryMap", 0))).as("countryMap")


            );
            return mongoTemplate.aggregate(aggregation, "entry", DashboardPayload.class).getUniqueMappedResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Some error occurred", e);
        }
    }


}

