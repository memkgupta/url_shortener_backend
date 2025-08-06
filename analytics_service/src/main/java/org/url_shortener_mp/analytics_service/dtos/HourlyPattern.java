package org.url_shortener_mp.analytics_service.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HourlyPattern implements Serializable {
    List<HourlyClick> pattern;
}
