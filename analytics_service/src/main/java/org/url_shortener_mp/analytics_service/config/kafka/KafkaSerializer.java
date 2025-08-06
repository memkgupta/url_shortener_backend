package org.url_shortener_mp.analytics_service.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public byte[] serialize(String s, T t) {
        try {
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsBytes(t);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
