package com.url_shortner.user_service.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
    private String access_token;
    private String refresh_token;
}
