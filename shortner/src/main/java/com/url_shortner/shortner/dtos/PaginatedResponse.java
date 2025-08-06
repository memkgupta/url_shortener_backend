package com.url_shortner.shortner.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data

public class PaginatedResponse<T> {
    private int total;
    private List<T> data;
    private Integer next;
    private Integer previous;
}
