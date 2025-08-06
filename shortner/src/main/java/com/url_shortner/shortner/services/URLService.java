package com.url_shortner.shortner.services;

import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;
import com.url_shortner.shortner.config.Base62Encoder;
import com.url_shortner.shortner.config.Counter;
import com.url_shortner.shortner.config.MD5HashGenerator;
import com.url_shortner.shortner.dtos.*;
import com.url_shortner.shortner.enitities.QR;
import com.url_shortner.shortner.enitities.URL;
import com.url_shortner.shortner.repositories.QRRepository;
import com.url_shortner.shortner.repositories.UrlRepository;
import com.url_shortner.shortner.utils.MonthTimestamps;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.google.zxing.*;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static com.url_shortner.shortner.utils.MonthTimestamps.*;

@Service
@RequiredArgsConstructor
public class URLService {
    private final UrlRepository urlRepository;
    private final QRRepository qrRepository;
    private final Counter counter;

    public URL createURL(CreateURLRequest request, String userId) {
        URL newURL = new URL();
        newURL.setCreated_at(request.getCreated_at() != null ? request.getCreated_at() : new Timestamp(System.currentTimeMillis()));
        if (request.getExpiryTime() != null) {
            newURL.setExpired_at(request.getExpiryTime());
        }
        String token = Base62Encoder.encode(counter.getCounter());
        newURL.setShortCode(token);
        newURL.setUrl(request.getLong_url());
        newURL.setUser_id(userId);
        return urlRepository.save(newURL);

    }

    @Cacheable(value = "urls", key = "#shortCode", unless = "#result == null")
    public URL fetchURL(String shortCode) {

        return urlRepository.findByShortCode(shortCode).orElse(null);
    }

    public QR attachQR(CreateQRRequest request, String userId) {
        URL url = fetchURL(request.getUrlId());
        if (url == null) {
            throw new RuntimeException("URL not found");
        }
        if (url.getQr() != null) {
            throw new RuntimeException("QR already exists");
        }
        QR qr = new QR();
        qr.setUrl(url);
        qr.setUser_id(userId);
        qr.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return qrRepository.save(qr);
    }

    public URL fetchURLById(String urlId) {
        return urlRepository.findById(urlId).orElse(null);
    }

    public boolean authoriseURLOwner(String urlId, String userId) {
        URL url = fetchURLById(urlId);

        if (url == null) {
            throw new RuntimeException("URL not found");
        }
        if (url.getUser_id().equals(userId)) {
            return true;
        }
        return false;
    }

    public Page<URL> getURLs(Pageable pageable, Specification<URL> specifications) {
        return urlRepository.findAll(specifications, pageable);
    }

    public void updateClicks(String urlId, long newClicks) {
        URL url = fetchURLById(urlId);
        if (url == null) {
            throw new RuntimeException("URL not found");
        }
        url.setClicks(url.getClicks() + newClicks);
        urlRepository.save(url);
    }

    public ComparableMetric getMetric(String metric, String userId) {

        return switch (metric) {
            case "clicks_month": {

                Long current = urlRepository.getTotalClicks(getStartOfCurrentMonth(), getEndOfCurrentMonth(), userId);
                Long previous = urlRepository.getTotalClicks(getStartOfLastMonth(), getEndOfLastMonth(), userId);
                yield ComparableMetric.builder()
                        .current(current)
                        .previous(previous)
                        .unit(TimeUnit.MONTH)
                        .build();
            }
            case "clicks_day": {
                Long current = urlRepository.getTotalClicks(getStartOfToday(), getEndOfToday(), userId);
                Long previous = urlRepository.getTotalClicks(getStartOfPreviousDay(), getEndOfPreviousDay(), userId);
                yield ComparableMetric.builder()
                        .current(current)
                        .previous(previous)
                        .unit(TimeUnit.DAY)
                        .build();
            }
            case "links_month": {
                Long current = urlRepository.getTotalUrlsCreated(getStartOfCurrentMonth(), getEndOfCurrentMonth(), userId);
                Long previous = urlRepository.getTotalUrlsCreated(getStartOfLastMonth(), getEndOfLastMonth(), userId);
                yield ComparableMetric.builder()
                        .current(current)
                        .previous(previous)
                        .unit(TimeUnit.MONTH)
                        .build();
            }
            default:
                throw new RuntimeException("Invalid metric");
        };
    }
}
