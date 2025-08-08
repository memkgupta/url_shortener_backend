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
@Data
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


    private Long clicks;
    private Timestamp timestamp;


}
