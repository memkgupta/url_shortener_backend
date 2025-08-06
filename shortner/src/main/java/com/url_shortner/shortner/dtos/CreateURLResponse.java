package com.url_shortner.shortner.dtos;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class CreateURLResponse {
 private String id;
 private String originalURL;
 private Timestamp timestamp;
}
