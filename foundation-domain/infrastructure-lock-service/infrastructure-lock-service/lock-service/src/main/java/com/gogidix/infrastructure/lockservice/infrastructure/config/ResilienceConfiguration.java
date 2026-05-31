package com.gogidix.infrastructure.lockservice.infrastructure.config;

import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration for Resilience4j retry policies.
 */
@Configuration
public class ResilienceConfiguration {

    @Value("${resilience4j.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${resilience4j.retry.wait-duration:100ms}")
    private Duration waitDuration;

    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig config = RetryConfig.custom()
            .maxAttempts(maxAttempts)
            .waitDuration(waitDuration)
            .retryExceptions(Exception.class)
            .ignoreExceptions(IllegalArgumentException.class)
            .build();

        return RetryRegistry.of(config);
    }
}
