package org.url_shortener_mp.analytics_service.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;

@Data
public class URLClickEventDTO {
    private String id;
    private String short_url;
    private String long_url;
    private String ip;
    private Timestamp timestamp;
    private String referrer;
    private String agent;
    private String browser;
    private String platform;
    private String device;
    private String os;
    private String country;


    @Override
    public String toString() {
        return "URLClickEventDTO{" +
                "id='" + id + '\'' +
                ", short_url='" + short_url + '\'' +
                ", long_url='" + long_url + '\'' +
                ", ip='" + ip + '\'' +
                ", timestamp=" + timestamp +
                ", referrer='" + referrer + '\'' +
                ", agent='" + agent + '\'' +
                '}';
    }
}
