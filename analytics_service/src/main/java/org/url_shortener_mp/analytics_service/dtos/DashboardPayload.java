package org.url_shortener_mp.analytics_service.dtos;

import java.io.Serializable;
import java.util.Map;


public class DashboardPayload implements Serializable {
    @Override
    public String toString() {
        return "DashboardPayload{" +
                "_id='" + _id + '\'' +
                ", totalClicks=" + totalClicks +
                ", agentMap=" + agentMap +
                ", browserMap=" + browserMap +
                ", osMap=" + osMap +
                ", deviceMap=" + deviceMap +
                ", countryMap=" + countryMap +
                '}';
    }

    private String _id;
    private long totalClicks;
    private Map<String, Long> agentMap;
    private Map<String, Long> browserMap;
    private Map<String, Long> osMap;
    private Map<String, Long> deviceMap;
    private Map<String, Long> countryMap;

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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public Map<String, Long> getAgentMap() {
        return agentMap;
    }

    public void setAgentMap(Map<String, Long> agentMap) {
        this.agentMap = agentMap;
    }
}
