package org.url_shortener_mp.api_gateway.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/v1/auth/me")
    Map<String, Object> getUserInfo(@RequestHeader("Authorization") String token);
}
