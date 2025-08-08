package org.url_shortener_mp.analytics_service.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.url_shortener_mp.analytics_service.dtos.URLClickEventDTO;


public class KafkaDeserializer<T> implements Deserializer<T> {
    private final Class<T> clazz;

    public KafkaDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public T deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return mapper.readValue(bytes, clazz);
        } catch (Exception e) {
            System.out.println("❌ Failed to deserialize: " + new String(bytes));
            e.printStackTrace();
            throw new RuntimeException("Error de serializing the dto", e);

        }
    }
}
