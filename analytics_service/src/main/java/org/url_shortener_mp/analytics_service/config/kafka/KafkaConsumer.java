package org.url_shortener_mp.analytics_service.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.url_shortener_mp.analytics_service.dtos.URLClickEventDTO;

@Component
public class KafkaConsumer {
//    @KafkaListener(topics = "url-click-event", groupId = "analytics-group-id-2")
//    public void listen(URLClickEventDTO h) {
//        System.out.println("Hello");
//        System.out.println(h);
//    }
}
