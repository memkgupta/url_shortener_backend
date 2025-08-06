package com.url_shortner.shortner.dtos;

import lombok.Data;

@Data
public class CreateQRRequest {
    private String urlId;

    private int height=300;
    private int width=300;
    private String color;
    private String url;

}
