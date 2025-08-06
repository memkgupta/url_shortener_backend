package com.url_shortner.user_service.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginRequest {
    private String email;
    private String password;
    private String code;

}
