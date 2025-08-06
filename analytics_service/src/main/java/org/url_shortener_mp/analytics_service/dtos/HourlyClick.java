package org.url_shortener_mp.analytics_service.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class HourlyClick implements Serializable {
    String hour;
    long clicks;
}
