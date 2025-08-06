package com.url_shortner.shortner.config.kafka;

import com.url_shortner.shortner.dtos.URLClickEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, URLClickEventDTO> kafkaTemplate;

    public void produce(String topic, URLClickEventDTO urlClickEventDTO) {
        Message<URLClickEventDTO> message = MessageBuilder.withPayload(urlClickEventDTO)
                .setHeader(KafkaHeaders.TOPIC,"url-click-event")
                .setHeader(KafkaHeaders.KEY,urlClickEventDTO.getId())
                .build();
        try{
            CompletableFuture<SendResult<String, URLClickEventDTO>> future = kafkaTemplate.send(message);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    System.out.println("Failed to send message: " + ex.getMessage());
                } else {
                    System.out.println(result.getProducerRecord());
                    System.out.println("Successfully sent to partition: " + result.getRecordMetadata().partition());
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
}
