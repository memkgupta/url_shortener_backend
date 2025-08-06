package com.url_shortner.shortner.config.feign;

import com.url_shortner.shortner.dtos.DashboardPayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;

@FeignClient(name = "analytics-service",url = "http://localhost:9009/v1")
public interface AnalyticsServiceClient {

    @GetMapping("/{id}")
   ResponseEntity<DashboardPayload> getDashboard(@PathVariable("id") String id, @RequestHeader("Authorisation") String authorisation , @RequestParam(name = "startTime") Timestamp startTime, @RequestParam(name = "endTime") Timestamp endTime);
}
