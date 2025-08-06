package org.url_shortener_mp.analytics_service.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Document
public class Entry {
    @Id
    private String id;
    @Field(name = "url", write = Field.Write.NON_NULL)
    private String url;
    private Timestamp startTime;
    private Timestamp endTime;
    private Map<String, Long> agentMap;
    private Map<String, Long> browserMap;
    private Map<String, Long> osMap;
    private Map<String, Long> deviceMap;
    private Map<String, Long> geoMap;
    private Map<String, Long> countryMap;
    private Map<String, Long> platformMap;
    private Map<String, Long> referrerMap;

    public Map<String, Long> getReferrerMap() {
        return referrerMap;
    }

    public void setReferrerMap(Map<String, Long> referrerMap) {
        this.referrerMap = referrerMap;
    }

    public Map<String, Long> getPlatformMap() {
        return platformMap;
    }

    public void setPlatformMap(Map<String, Long> platformMap) {
        this.platformMap = platformMap;
    }

    private Long clicks;
    private Timestamp timestamp;

    public Map<String, Long> getBrowserMap() {
        return browserMap;
    }

    public void setBrowserMap(Map<String, Long> browserMap) {
        this.browserMap = browserMap;
    }

    public Map<String, Long> getOsMap() {
        return osMap;
    }

    public void setOsMap(Map<String, Long> osMap) {
        this.osMap = osMap;
    }

    public Map<String, Long> getCountryMap() {
        return countryMap;
    }

    public void setCountryMap(Map<String, Long> countryMap) {
        this.countryMap = countryMap;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Map<String, Long> getAgentMap() {
        return agentMap;
    }

    public void setAgentMap(Map<String, Long> agentMap) {
        this.agentMap = agentMap;
    }

    public Map<String, Long> getDeviceMap() {
        return deviceMap;
    }

    public void setDeviceMap(Map<String, Long> deviceMap) {
        this.deviceMap = deviceMap;
    }

    public Map<String, Long> getGeoMap() {
        return geoMap;
    }

    public void setGeoMap(Map<String, Long> geoMap) {
        this.geoMap = geoMap;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }
}
