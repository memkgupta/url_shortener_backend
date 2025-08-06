package com.url_shortner.shortner.controllers;

import com.url_shortner.shortner.dtos.DashboardOverview;
import com.url_shortner.shortner.services.QRService;
import com.url_shortner.shortner.services.URLService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/shortener")
@RequiredArgsConstructor
public class DashboardController {
    private final URLService urlService;
    private final QRService qrService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardOverview> getDashboard(@RequestHeader(name = "X-USER-ID") String userId) {
        System.out.println("Request came");
        return
                ResponseEntity.ok(
                        DashboardOverview.builder()
                                .total_clicks(urlService.getMetric("clicks_month", userId))
                                .total_clicks_today(urlService.getMetric("clicks_day", userId))
                                .total_links_created(urlService.getMetric("links_month", userId))
                                .total_qr_codes(qrService.getMetric("qr_codes_month", userId))
                                .build()
                );
    }
}
