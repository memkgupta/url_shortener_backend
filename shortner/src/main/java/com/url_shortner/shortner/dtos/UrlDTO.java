package com.url_shortner.shortner.dtos;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Builder
public class UrlDTO {
    private String id;
    private String originalURL;
    private Timestamp createdAt;
    private String shortURL;
    private String shortCode;
    private boolean hasQrCode;
    private long clicks;
    private DashboardPayload analyticsOverview;
}
