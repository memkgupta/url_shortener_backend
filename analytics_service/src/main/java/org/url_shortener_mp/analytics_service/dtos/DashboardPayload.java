package org.url_shortener_mp.analytics_service.dtos;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;


@Data
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

}
