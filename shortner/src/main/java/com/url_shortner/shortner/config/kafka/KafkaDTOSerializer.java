package com.url_shortner.shortner.config.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.url_shortner.shortner.dtos.URLClickEventDTO;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaDTOSerializer implements Serializer<URLClickEventDTO> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(String s, URLClickEventDTO urlClickEventDTO) {
       try {
        return objectMapper.writeValueAsBytes(urlClickEventDTO);
       }
       catch (Exception e) {
           throw new RuntimeException("Error serializing URLClickEventDTO", e);
       }
    }
}
