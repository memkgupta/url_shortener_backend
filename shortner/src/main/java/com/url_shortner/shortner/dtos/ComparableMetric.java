package com.url_shortner.shortner.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparableMetric {
    private long current;
    private long previous;
    private TimeUnit unit;
}
