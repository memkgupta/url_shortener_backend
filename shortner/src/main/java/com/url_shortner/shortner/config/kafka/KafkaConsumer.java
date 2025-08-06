package com.url_shortner.shortner.config.kafka;

import com.url_shortner.shortner.dtos.WindowedAnalytic;
import com.url_shortner.shortner.services.URLService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private final URLService urlService;

    public KafkaConsumer(URLService urlService) {
        this.urlService = urlService;
    }

    @KafkaListener(topics = "analytic_window", groupId = "consumer-group-db-persistence")
    public void listen(WindowedAnalytic windowedAnalytic) {
        urlService.updateClicks(windowedAnalytic.getShortUrl(), windowedAnalytic.getTotalClicks());
    }
}
