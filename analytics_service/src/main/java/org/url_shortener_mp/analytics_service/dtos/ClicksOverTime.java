package org.url_shortener_mp.analytics_service.dtos;

import lombok.Data;
;import java.io.Serializable;
import java.util.List;

@Data
public class ClicksOverTime implements Serializable {
    private int length;
    private List<TimelyClick> clicks;
    private TimeUnit timeUnit;
}