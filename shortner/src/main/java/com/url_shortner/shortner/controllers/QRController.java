package com.url_shortner.shortner.controllers;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.url_shortner.shortner.dtos.CreateQRRequest;
import com.url_shortner.shortner.dtos.CreateQRResponse;
import com.url_shortner.shortner.enitities.QR;
import com.url_shortner.shortner.services.QRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;

@RestController
@RequestMapping("/v1/shortener/qr")
@RequiredArgsConstructor
public class QRController {
    private final QRService qrService;

    @PostMapping
    public ResponseEntity<CreateQRResponse> createQR(@RequestBody CreateQRRequest request,
                                                     @RequestHeader(value = "X-USER-ID", required = true) String userId) {
        QR qr = qrService.createQRFromExternalURL(request, userId);
        return ResponseEntity.ok(
                CreateQRResponse.builder()
                        .id(qr.getId())
                        .url_id(qr.getUrl().getId())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StreamingResponseBody> getQR(@PathVariable String id, @RequestParam(defaultValue = "300") int height,
                                                       @RequestParam(defaultValue = "300") int width) {
        try {
            BitMatrix bitMatrix = qrService.getQRImage(
                    id, width, height
            );
            StreamingResponseBody streamingResponseBody = out ->
                    MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(streamingResponseBody);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
    }
}
