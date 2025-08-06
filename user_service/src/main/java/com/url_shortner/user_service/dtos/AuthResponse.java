package com.url_shortner.user_service.dtos;

import com.url_shortner.user_service.entities.Token;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {
    private TokenDTO tokens;
    private String name;
    private String id;
    private String role;
    private String email;
    private String profile;
}
