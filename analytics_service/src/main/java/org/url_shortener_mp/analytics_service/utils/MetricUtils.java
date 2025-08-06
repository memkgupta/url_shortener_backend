package org.url_shortener_mp.analytics_service.utils;

import org.url_shortener_mp.analytics_service.dtos.QueryParameters;
import org.url_shortener_mp.analytics_service.dtos.WindowUnit;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

public class MetricUtils {
    private static Map<String, String> metricsMap
            = Map.of(

            "clicks", "clicks",
            "referrers", "referrerMap",
            "agents", "agentMap",
            "os", "osMap",
            "country", "countryMap",
            "browser", "browserMap",
            "geo", "geoMap",
            "devices", "deviceMap"
    );

    public static QueryParameters extractParamerters(Map<String, String> params) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters
                .setMetric(params.get("metric"));
        Instant instant = Instant.parse(params.get("startTime")); // Parses ISO-8601 with Z
        Timestamp ts = Timestamp.from(instant);
        queryParameters
                .setStartTime(ts);
        Instant instant1 = Instant.parse(params.get("endTime")); // Parses ISO-8601 with Z
        Timestamp te = Timestamp.from(instant1);
        queryParameters.setEndTime(te);
        queryParameters.setUrlId(params.get("urlId"));
        queryParameters.setWindowUnits(WindowUnit.valueOf(params.get("unit")));
        return queryParameters;
    }

    public static String getEntryMapping(String metric) {
        return metricsMap.get(metric);
    }
}
