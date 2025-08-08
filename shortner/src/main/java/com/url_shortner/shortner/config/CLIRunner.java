package com.url_shortner.shortner.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CLIRunner {
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
}