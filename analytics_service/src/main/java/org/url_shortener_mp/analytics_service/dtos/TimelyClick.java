package org.url_shortener_mp.analytics_service.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class TimelyClick implements Serializable {
    private String time;
    private int clicks;
    private Integer uniqueClicks; // optional
}
