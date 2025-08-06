package com.url_shortner.shortner.services;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.url_shortner.shortner.dtos.CreateQRRequest;
import com.url_shortner.shortner.enitities.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class QRCodeService {
    private final URLService urlService;
    public BitMatrix generateQRCode(String content , int height , int width) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
BitMatrix bitMatrix = qrCodeWriter.encode(
            content,null,width,height
        );
        return bitMatrix;
    }

}
