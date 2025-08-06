package com.url_shortner.shortner.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class Range {
    @Value("${app.counter.url}")
    private String COUNTER_URL;
    RestTemplate restTemplate;
    long start;
    long end;

    public Range() {
        restTemplate = new RestTemplate();
    }
    @PostConstruct
    public void init(){
        try {
           ResponseEntity<String> response = restTemplate.getForEntity(COUNTER_URL, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
               start = Long.parseLong(response.getBody());
               end = start+100000-1;
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to load data: " + e.getMessage());
        }
    }

    public void requestNewRange(){
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://counter-service:8085/get", String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                start = Long.parseLong(response.getBody());
                end = start+100000;
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to load data: " + e.getMessage());
        }
    }
}
