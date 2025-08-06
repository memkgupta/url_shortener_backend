package org.url_shortener_mp.analytics_service.dtos;

import lombok.Data;
import org.springframework.data.mongodb.core.aggregation.SetWindowFieldsOperation;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Data
public class QueryParameters {
    private String urlId;
    private Timestamp startTime;
    private Timestamp endTime;
    private WindowUnit windowUnits;
    private String metric;
}
