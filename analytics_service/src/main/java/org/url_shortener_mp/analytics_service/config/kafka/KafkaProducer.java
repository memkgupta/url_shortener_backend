package org.url_shortener_mp.analytics_service.config.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.url_shortener_mp.analytics_service.dtos.WindowedAnalytic;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, WindowedAnalytic> kafkaTemplate;
    public KafkaProducer(KafkaTemplate<String, WindowedAnalytic> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void send( WindowedAnalytic windowedAnalytic) {
        Message<WindowedAnalytic> message = MessageBuilder.withPayload(windowedAnalytic)
                .setHeader(KafkaHeaders.TOPIC,"analytic_window")
                .setHeader(KafkaHeaders.KEY,windowedAnalytic.getShortUrl())
                .build();
      try{
          CompletableFuture<SendResult<String,WindowedAnalytic>> future = kafkaTemplate.send(message);
          future.whenComplete((result,ex)->{
              if(ex!=null){
                  System.out.println("Failed to send message: " + ex.getMessage());
              }

          });
      }
      catch(Exception e){
          e.printStackTrace();
      }
    }
}
