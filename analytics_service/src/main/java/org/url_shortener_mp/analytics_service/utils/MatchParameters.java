package org.url_shortener_mp.analytics_service.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchParameters <T>{
    private String name;
    private T value;
    private CriteriaComparator<T> criteriaComparator;
}
