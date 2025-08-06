package org.url_shortener_mp.analytics_service.dtos;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;

public class RangeAnalyticData {
    private Timestamp startTime;
    private Timestamp endTime;
    private double clicks;
    private Map<String,Long> agentMap;
    private Map<String,Long> referrerMap;
    private Map<String,Long> osMap;
    private Map<String,Long> deviceMap;
    private Map<String,Long> countryMap;

    public Map<String, Long> getOsMap() {
        return osMap;
    }

    public void setOsMap(Map<String, Long> osMap) {
        this.osMap = osMap;
    }

    public Map<String, Long> getDeviceMap() {
        return deviceMap;
    }

    public void setDeviceMap(Map<String, Long> deviceMap) {
        this.deviceMap = deviceMap;
    }

    public Map<String, Long> getCountryMap() {
        return countryMap;
    }

    public void setCountryMap(Map<String, Long> countryMap) {
        this.countryMap = countryMap;
    }

    public Map<String, Long> getReferrerMap() {
        return referrerMap;
    }

    public void setReferrerMap(Map<String, Long> referrerMap) {
        this.referrerMap = referrerMap;
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

    public double getClicks() {
        return clicks;
    }

    public void setClicks(double clicks) {
        this.clicks = clicks;
    }

    public Map<String, Long> getAgentMap() {
        return agentMap;
    }

    public void setAgentMap(Map<String, Long> agentMap) {
        this.agentMap = agentMap;
    }
}
