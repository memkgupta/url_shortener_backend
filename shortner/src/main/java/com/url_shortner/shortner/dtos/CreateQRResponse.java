package com.url_shortner.shortner.dtos;

import lombok.Builder;

@Builder
public class CreateQRResponse {
    private String id;
    private String url_id;

}
