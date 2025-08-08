package org.url_shortener_mp.analytics_service.dtos;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
public class WindowedAnalytic {
    private String shortUrl;
    private Timestamp windowStart;
    private Timestamp windowEnd;
    private long totalClicks;
    private Map<String, Long> referrerCounts;
    private Map<String, Long> userAgentCounts;
    private Map<String, Long> browserCounts;
    private Map<String, Long> platformCounts;
    private Map<String, Long> deviceCounts;
    private Map<String, Long> osCounts;
    private Map<String, Long> countryCounts;

    @Override
    public String toString() {
        return "WindowedAnalytic{" +
                "shortUrl='" + shortUrl + '\'' +
                ", windowStart=" + windowStart +
                ", windowEnd=" + windowEnd +
                ", totalClicks=" + totalClicks +
                ", referrerCounts=" + referrerCounts +
                ", userAgentCounts=" + userAgentCounts +
                ", browserCounts=" + browserCounts +
                ", platformCounts=" + platformCounts +
                ", deviceCounts=" + deviceCounts +
                ", osCounts=" + osCounts +
                ", countryCounts=" + countryCounts +
                '}';
    }


    public WindowedAnalytic() {
        referrerCounts = new HashMap<>();
        userAgentCounts = new HashMap<>();
        browserCounts = new HashMap<>();
        platformCounts = new HashMap<>();
        deviceCounts = new HashMap<>();
        osCounts = new HashMap<>();
        countryCounts = new HashMap<>();

    }


}
