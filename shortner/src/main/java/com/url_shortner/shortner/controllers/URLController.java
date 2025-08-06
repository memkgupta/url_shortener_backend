package com.url_shortner.shortner.controllers;

import com.url_shortner.shortner.config.feign.AnalyticsServiceClient;
import com.url_shortner.shortner.dtos.*;
import com.url_shortner.shortner.enitities.QR;
import com.url_shortner.shortner.enitities.URL;
import com.url_shortner.shortner.services.URLService;
import com.url_shortner.shortner.utils.PaginationUtil;
import com.url_shortner.shortner.utils.mapper.URLMapper;
import com.url_shortner.shortner.utils.specifications.implementations.URLSpecificationStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/v1/url")
@RequiredArgsConstructor
public class URLController {
    private final URLService urlService;
    private final AnalyticsServiceClient analyticsServiceClient;
    private final URLSpecificationStrategyFactory specFactory;

    @PostMapping("/create")
    public CreateURLResponse createURL(@RequestBody CreateURLRequest request, @RequestHeader(name = "X-USER-ID") String userId) {

        URL url = urlService.createURL(request, userId);
        return CreateURLResponse.builder()
                .originalURL("http://localhost:8001/s/" + url.getShortCode())
                .id(url.getId())
                .timestamp(url.getCreated_at())
                .build();
    }

    @PostMapping("/attach-qr")
    public ResponseEntity<CreateQRResponse> createQR(@RequestBody CreateQRRequest request, @RequestHeader(name = "X-USER-ID", required = true) String userId) {
        QR qr = urlService.attachQR(request, userId);
        return ResponseEntity.ok(
                CreateQRResponse.builder()
                        .url_id(qr.getUrl().getId())
                        .id(qr.getId())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlDTO> getURLDetails(@PathVariable String id, @RequestHeader(name = "X-USER-ID", required = true) String userId, @RequestParam(name = "startTime", required = false) Timestamp startTime,
                                                @RequestParam(name = "endTime", required = false) Timestamp endTime) {
        URL url = urlService.fetchURLById(id);
        if (url == null || !url.getUser_id().equals(userId)) {
            throw new RuntimeException("URL not found");
        }
        if (startTime == null) {
            startTime = Timestamp.valueOf(java.time.LocalDateTime.now().minusDays(30));
        }

        // If endTime is null, set it to current time
        if (endTime == null) {
            endTime = new Timestamp(System.currentTimeMillis());
        }
        DashboardPayload dashboardPayload = analyticsServiceClient.getDashboard(url.getId(),
                "Bearer SERVICE_AUTH", startTime, endTime
        ).getBody();
        if (dashboardPayload == null) {
            throw new RuntimeException("DASHBOARD NOT FOUND");
        }
        return ResponseEntity.ok(
                UrlDTO.builder()
                        .id(url.getId())
                        .shortCode(url.getShortCode())
                        .hasQrCode(url.getQr() != null)
                        .originalURL(url.getUrl())
                        .analyticsOverview(dashboardPayload)
                        .shortURL("http://localhost:8001/s/" + url.getShortCode())
                        .createdAt(url.getCreated_at())
                        .build()
        );
    }

    @GetMapping("/details/all")
    public ResponseEntity<PaginatedResponse<UrlDTO>> getURLs(@RequestHeader(name = "X-USER-ID", required = true) String userId, @RequestParam(name = "original_url", required = false) String orginalUrl,
                                                             @RequestParam(name = "shortCode", required = false) String shortCode,
                                                             @RequestParam(name = "created_at_start", required = false) Timestamp startCreatedAt,
                                                             @RequestParam(name = "created_at_end", required = false) Timestamp endCreatedAt,
                                                             @RequestParam(name = "tags", required = false) Set<String> tags,
                                                             @RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                                             @RequestParam(name = "size", defaultValue = "20", required = false) int size
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Specification<URL> specification = specFactory.getSpecification("url_user_id", userId);
        if (orginalUrl != null) {
            specification = specification.and(specFactory.getSpecification("original_url", orginalUrl));
        }
        if (shortCode != null) {
            specification = specification.and(specFactory.getSpecification("short_code", shortCode));
        }
        if (startCreatedAt != null) {
            specification = specification.and(specFactory.getSpecification("created_at", startCreatedAt));
        }
        if (endCreatedAt != null) {
            specification = specification.and(specFactory.getSpecification("created_at", endCreatedAt));
        }
        if (tags != null && tags.size() > 0) {
            specification = specification.and(specFactory.getSpecification("tags", tags));
        }

        Page<URL> pageResponse = urlService.getURLs(pageable, specification);
        PaginatedResponse<UrlDTO> paginatedResponse = PaginationUtil.toPaginatedResponse(pageResponse, new URLMapper());
        return ResponseEntity.ok(paginatedResponse);
    }

   
}
