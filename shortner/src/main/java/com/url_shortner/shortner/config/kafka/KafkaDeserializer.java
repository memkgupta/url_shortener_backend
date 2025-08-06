package com.url_shortner.shortner.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.url_shortner.shortner.dtos.WindowedAnalytic;
import org.apache.kafka.common.serialization.Deserializer;

public class KafkaDeserializer implements Deserializer<WindowedAnalytic> {
    private final ObjectMapper mapper;

    public KafkaDeserializer() {
        this.mapper = new ObjectMapper();
        // ✅ Register support for Java 8 date/time types
        this.mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public WindowedAnalytic deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            WindowedAnalytic dto = mapper.readValue(bytes, WindowedAnalytic.class);
            return dto;
        } catch (Exception e) {
            System.out.println("❌ Failed to deserialize: " + new String(bytes));
            e.printStackTrace();
            throw new RuntimeException("Error de serializing the dto", e);

        }
    }
}
