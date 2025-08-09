package com.url_shortner.shortner.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@OpenAPIDefinition
public class AppConfig {
    @Bean
    public CommandLineRunner runner(Environment env) {
        return args -> {
            System.out.println("📦 DB_URL = " + env.getProperty("DB_URL"));
            System.out.println("📦 Final DB URL = " + env.getProperty("spring.datasource.url"));
            System.out.println("📦 REDIS_HOST = " + env.getProperty("REDIS_HOST"));
            System.out.println("📦 KAFKA_SERVER_URL = " + env.getProperty("KAFKA_SERVER_URL"));
            System.out.println("📦 COUNTER_URL = " + env.getProperty("app.counter.url"));
        };
    }

    @Bean
    public OpenAPI userOpenAPI(
            @Value("${openapi.service.title}") String serviceTitle,
            @Value("${openapi.service.version}") String serviceVersion,
            @Value("${openapi.service.url}") String url) {
        return new OpenAPI()
                .servers(List.of(new Server().url(url)))
                .info(new Info().title(serviceTitle).version(serviceVersion));
    }
}