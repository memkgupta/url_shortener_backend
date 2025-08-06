package com.url_shortner.shortner.dtos;

import lombok.*;

import java.sql.Timestamp;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateURLRequest {
    private String long_url;

    private Timestamp created_at;

    private Timestamp expiryTime;

}
