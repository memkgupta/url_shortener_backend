package com.url_shortner.shortner.config;

import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


    @Configuration
    public class URLAnalyzerConfig {
        @Bean
        public UserAgentAnalyzer userAgentAnalyzer() {
            return UserAgentAnalyzer
                    .newBuilder()
                    .withCache(10000)
                  
                    .build();
        }
    }

