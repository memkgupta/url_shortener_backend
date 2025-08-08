package org.url_shortener_mp.analytics_service.config.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.url_shortener_mp.analytics_service.dtos.URLClickEventDTO;

public class URLClickEventSerializerDeserializer implements Serde<URLClickEventDTO> {
    @Override
    public Serializer<URLClickEventDTO> serializer() {
        return new Serializer<URLClickEventDTO>() {
            final ObjectMapper objectMapper = new ObjectMapper();

            @Override
            public byte[] serialize(String s, URLClickEventDTO urlClickEventDTO) {
                try {
                    return objectMapper.writeValueAsBytes(urlClickEventDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public Deserializer<URLClickEventDTO> deserializer() {
        return new KafkaDeserializer<URLClickEventDTO>(URLClickEventDTO.class);
    }
}
