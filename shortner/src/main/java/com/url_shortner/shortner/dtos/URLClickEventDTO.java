package com.url_shortner.shortner.dtos;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
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
}
