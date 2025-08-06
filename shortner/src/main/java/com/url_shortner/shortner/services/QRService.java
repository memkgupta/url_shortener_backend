package com.url_shortner.shortner.services;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.url_shortner.shortner.dtos.ComparableMetric;
import com.url_shortner.shortner.dtos.CreateQRRequest;
import com.url_shortner.shortner.dtos.CreateURLRequest;
import com.url_shortner.shortner.dtos.TimeUnit;
import com.url_shortner.shortner.enitities.QR;
import com.url_shortner.shortner.enitities.URL;
import com.url_shortner.shortner.repositories.QRRepository;
import com.url_shortner.shortner.repositories.UrlRepository;
import com.url_shortner.shortner.utils.MonthTimestamps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import static com.url_shortner.shortner.utils.MonthTimestamps.*;

@Service
@RequiredArgsConstructor
public class QRService {
    private final QRRepository qrRepository;
    private final URLService urlService;
    private final UrlRepository urlRepository;
    private final QRCodeService qrCodeService;
    @Value("${app.api_gateway.url}")
    private String API_GATEWAY_HOST;

    public QR saveQR(QR qr) {
        return qrRepository.save(qr);
    }

    public QR getQR(String id) {
        return qrRepository.findById(id).orElse(null);
    }

    public QR createQRFromExternalURL(CreateQRRequest createQRRequest, String userId) {
        URL url = urlService.createURL(
                CreateURLRequest.builder()
                        .created_at(new Timestamp(System.currentTimeMillis()))
                        .long_url(createQRRequest.getUrl())


                        .build(), userId
        );
        QR qr = new QR();
        qr.setUrl(url);
        qr.setUser_id(userId);
        qr.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        url.setQr(qr);
        urlRepository.save(url);
        return qr;
    }

    public BitMatrix getQRImage(String qr_id, int width, int height) throws IOException, WriterException {
        QR qr = getQR(qr_id);
        if (qr == null) {
            throw new RuntimeException("QR not found");
        }
        String url = API_GATEWAY_HOST + "/s" + qr.getUrl().getShortCode() + "?src=qr";
        return qrCodeService.generateQRCode(
                url, height, width
        );
    }

    public ComparableMetric getMetric(String metric, String userId) {
        return switch (metric) {
            case "qr_codes_month": {
                Long current = qrRepository.getTotalQrsCreated(
                        userId,
                        MonthTimestamps.getStartOfCurrentMonth(),
                        getEndOfCurrentMonth()
                );
                Long previous =
                        qrRepository.getTotalQrsCreated(
                                userId,
                                getStartOfLastMonth(),
                                getEndOfLastMonth()
                        );
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
