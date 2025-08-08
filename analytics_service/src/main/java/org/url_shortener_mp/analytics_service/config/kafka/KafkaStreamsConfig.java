package org.url_shortener_mp.analytics_service.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.url_shortener_mp.analytics_service.dtos.URLClickEventDTO;
import org.url_shortener_mp.analytics_service.dtos.WindowedAnalytic;
import org.url_shortener_mp.analytics_service.services.AnalyticService;

import java.sql.Timestamp;
import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean
    public KStream<String, URLClickEventDTO> kStream(StreamsBuilder builder, AnalyticService analyticService, KafkaProducer producer) {
        KStream<String, URLClickEventDTO> kStream = builder.stream("url-click-event", Consumed.with(Serdes.String(), new URLClickEventSerializerDeserializer()));
        KTable<Windowed<String>, WindowedAnalytic> clickCounts = kStream.groupByKey().
                windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10)))
                .aggregate(WindowedAnalytic::new,
                        (shortUrl, event, aggregate) -> {

                            aggregate.setShortUrl(shortUrl);

                            aggregate.setTotalClicks(aggregate.getTotalClicks() + 1);

                            // Referrer counts
                            if (event.getReferrer() != null) {
                                aggregate.getReferrerCounts().put(event.getReferrer(), aggregate.getReferrerCounts().getOrDefault(event.getReferrer(), 0L) + 1);

                            }
                            if (event.getAgent() != null) {
                                String agentKey = event.getAgent().trim().toLowerCase().replace(".", "_");
                                aggregate.getUserAgentCounts()
                                        .put(agentKey, aggregate.getUserAgentCounts().getOrDefault(agentKey, 0L) + 1);
                            }

                            if (event.getBrowser() != null) {
                                aggregate.getBrowserCounts()
                                        .put(event.getBrowser(),
                                                aggregate.getBrowserCounts().getOrDefault(event.getBrowser(), 0L) + 1);
                            }


                            if (event.getPlatform() != null) {
                                aggregate.getPlatformCounts()
                                        .put(event.getPlatform(),
                                                aggregate.getPlatformCounts().getOrDefault(event.getPlatform(), 0L) + 1);
                            }


                            if (event.getDevice() != null) {
                                aggregate.getDeviceCounts()
                                        .put(event.getDevice(),
                                                aggregate.getDeviceCounts().getOrDefault(event.getDevice(), 0L) + 1);
                            }


                            if (event.getOs() != null) {
                                aggregate.getOsCounts()
                                        .put(event.getOs(),
                                                aggregate.getOsCounts().getOrDefault(event.getOs(), 0L) + 1);
                            }


                            if (event.getCountry() != null) {
                                aggregate.getCountryCounts()
                                        .put(event.getCountry(),
                                                aggregate.getCountryCounts().getOrDefault(event.getCountry(), 0L) + 1);
                            }

                            return aggregate;
                        }
                        , Materialized.with(Serdes.String(), new Serde<WindowedAnalytic>() {
                            @Override
                            public Serializer<WindowedAnalytic> serializer() {
                                return new Serializer<WindowedAnalytic>() {
                                    ObjectMapper objectMapper = new ObjectMapper();

                                    @Override
                                    public byte[] serialize(String s, WindowedAnalytic windowedAnalytic) {
                                        try {
                                            return objectMapper.writeValueAsBytes(windowedAnalytic);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            }

                            @Override
                            public Deserializer<WindowedAnalytic> deserializer() {
                                return new KafkaDeserializer<WindowedAnalytic>(WindowedAnalytic.class);
                            }

                            ObjectMapper mapper = new ObjectMapper();

                        }))
                .toStream()
                .peek((k, v) -> {
                    v.setWindowStart(Timestamp.from(k.window().startTime()));
                    v.setWindowEnd(Timestamp.from(k.window().endTime()));

                }).toTable();

        clickCounts.toStream().foreach((k, c) -> {
//            String key = k.key(); // key is not needed as such because i don't want the ordering as such

            producer.send(c); // sending the window to store the total clicks in my db ( in order to reduce the load of multiple analytics service api calls)
            analyticService.writeLog(c); // persisting the aggregated window in timeseries db
        });

        return kStream;
    }
}
