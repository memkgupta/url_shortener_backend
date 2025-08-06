package com.url_shortner.user_service.dtos;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String profile;
}
