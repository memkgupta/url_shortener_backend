package org.url_shortener_mp.analytics_service.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "shortener-service", url = "http://localhost:9010/v1")
public interface URLClient {
    @GetMapping("/authorisation/authorise")
    ResponseEntity<?> verifyOwner(@RequestParam String userId, @RequestParam String urlId, @RequestHeader("Authorization") String auth);
}
