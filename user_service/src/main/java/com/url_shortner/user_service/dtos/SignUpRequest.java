package com.url_shortner.user_service.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequest {
    private String name;
    private String password;
    private String email;
    @Nullable
    private String code;
}
