package com.url_shortner.user_service.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoDTO {
    private String id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String role;
}
