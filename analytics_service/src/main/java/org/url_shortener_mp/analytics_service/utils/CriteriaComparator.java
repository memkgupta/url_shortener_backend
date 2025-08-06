package org.url_shortener_mp.analytics_service.utils;

import org.springframework.data.mongodb.core.query.Criteria;

@FunctionalInterface
public interface CriteriaComparator<T> {
    public Criteria compare(Criteria query, Object value, String key);
}
