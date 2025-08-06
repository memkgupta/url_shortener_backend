package org.url_shortener_mp.analytics_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.url_shortener_mp.analytics_service.config.feign.URLClient;
import org.url_shortener_mp.analytics_service.dtos.ClicksOverTime;
import org.url_shortener_mp.analytics_service.dtos.DashboardPayload;
import org.url_shortener_mp.analytics_service.dtos.HourlyClick;
import org.url_shortener_mp.analytics_service.dtos.HourlyPattern;
import org.url_shortener_mp.analytics_service.services.AnalyticService;
import org.url_shortener_mp.analytics_service.utils.MetricUtils;


import java.net.http.HttpHeaders;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class Controller {


    private final AnalyticService analyticService;
    private final URLClient urlServiceClient;

    public Controller(AnalyticService analyticService, URLClient urlServiceClient) {
        this.analyticService = analyticService;
        this.urlServiceClient = urlServiceClient;
    }

    @GetMapping("/metric/clicks-over-time")
    public ResponseEntity<ClicksOverTime> getClicks(@RequestParam Map<String, String> allParams, @RequestHeader("X-USER-ID") String userId) {
        urlServiceClient.verifyOwner(userId,
                allParams.get("urlId"), "Bearer SERVICE_AUTH");
        return ResponseEntity.ok(analyticService.getCLicksOverTime(MetricUtils.extractParamerters(allParams)));
    }

    @GetMapping("/metric/hourly-pattern")
    public ResponseEntity<HourlyPattern> getHourlyClicks(@RequestParam Map<String, String> allParams, @RequestHeader("X-USER-ID") String userId) {
        urlServiceClient.verifyOwner(userId,
                allParams.get("urlId"), "Bearer SERVICE_AUTH");
        List<HourlyClick> pattern = analyticService.getHourlyClicks(MetricUtils.extractParamerters(allParams));
        HourlyPattern res = new HourlyPattern();
        res.setPattern(pattern);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/metric/browsers-over-time")
    public List<Map> getBrowsers(@RequestParam Map<String, String> allParams, @RequestHeader("X-USER-ID") String userId) {
        urlServiceClient.verifyOwner(userId,
                allParams.get("urlId"), "Bearer SERVICE_AUTH");
        return analyticService.getBrowsersOverTime(MetricUtils.extractParamerters(allParams));
    }

    @GetMapping("/metric/os-over-time")
    public List<Map> getOS(@RequestParam Map<String, String> allParams, @RequestHeader("X-USER-ID") String userId) {
        urlServiceClient.verifyOwner(userId,
                allParams.get("urlId"), "Bearer SERVICE_AUTH");
        return analyticService.getOperatingSystemsOverTime(MetricUtils.extractParamerters(allParams));
    }

    @GetMapping("/metric/referrer-over-time")
    public List<Map> getReferrer(@RequestParam Map<String, String> allParams, @RequestHeader("X-USER-ID") String userId) {
        urlServiceClient.verifyOwner(userId,
                allParams.get("urlId"), "Bearer SERVICE_AUTH");
        return analyticService.getReferrersOverTime(MetricUtils.extractParamerters(allParams));
    }

    // service call only hence this will be always authorised
    @GetMapping("/{id}")
    public ResponseEntity<?> getOverview(@PathVariable String id, @RequestHeader("Authorisation") String authorisation, @RequestParam(name = "startTime") Timestamp startTime, @RequestParam(name = "endTime") Timestamp endTime) {
        if (!authorisation.equals("Bearer SERVICE_AUTH")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        DashboardPayload payload = analyticService.getOverview(id, startTime, endTime);
        System.out.println("**************************************");
        System.out.println(payload);
        return ResponseEntity.ok(payload);
    }
}
