package com.gogidix.infrastructure.config.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Application configuration for Infrastructure Config Service.
 */
@Configuration
@EnableCaching
@EnableKafka
@EnableMongoRepositories(basePackages = "com.gogidix.infrastructure.config.domain.repository")
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
public class ApplicationConfig {

    /**
     * ObjectMapper for JSON serialization.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Auditor aware for capturing the current user.
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new CurrentUserAuditorAware();
    }

    /**
     * Rest template for external calls.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Custom MongoDB conversions.
     */
    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(
            new LocalDateTimeConverter(),
            new EnumConverter()
        ));
    }
}
