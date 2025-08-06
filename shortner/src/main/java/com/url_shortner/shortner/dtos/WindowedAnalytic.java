package com.url_shortner.shortner.dtos;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Long> getCountryCounts() {
        return countryCounts;
    }

    public void setCountryCounts(Map<String, Long> countryCounts) {
        this.countryCounts = countryCounts;
    }

    public Map<String, Long> getBrowserCounts() {
        return browserCounts;
    }

    public void setBrowserCounts(Map<String, Long> browserCounts) {
        this.browserCounts = browserCounts;
    }

    public Map<String, Long> getPlatformCounts() {
        return platformCounts;
    }

    public void setPlatformCounts(Map<String, Long> platformCounts) {
        this.platformCounts = platformCounts;
    }

    public Map<String, Long> getDeviceCounts() {
        return deviceCounts;
    }

    public void setDeviceCounts(Map<String, Long> deviceCounts) {
        this.deviceCounts = deviceCounts;
    }

    public Map<String, Long> getOsCounts() {
        return osCounts;
    }

    public void setOsCounts(Map<String, Long> osCounts) {
        this.osCounts = osCounts;
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


    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Timestamp getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(Timestamp windowStart) {
        this.windowStart = windowStart;
    }

    public Timestamp getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Timestamp windowEnd) {
        this.windowEnd = windowEnd;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public Map<String, Long> getReferrerCounts() {
        return referrerCounts;
    }

    public void setReferrerCounts(Map<String, Long> referrerCounts) {
        this.referrerCounts = referrerCounts;
    }

    public Map<String, Long> getUserAgentCounts() {
        return userAgentCounts;
    }

    public void setUserAgentCounts(Map<String, Long> userAgentCounts) {
        this.userAgentCounts = userAgentCounts;
    }
}
