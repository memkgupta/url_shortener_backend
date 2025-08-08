package org.url_shortener_mp.analytics_service.dtos;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;

@Data
public class RangeAnalyticData {
    private Timestamp startTime;
    private Timestamp endTime;
    private double clicks;
    private Map<String, Long> agentMap;
    private Map<String, Long> referrerMap;
    private Map<String, Long> osMap;
    private Map<String, Long> deviceMap;
    private Map<String, Long> countryMap;


}
