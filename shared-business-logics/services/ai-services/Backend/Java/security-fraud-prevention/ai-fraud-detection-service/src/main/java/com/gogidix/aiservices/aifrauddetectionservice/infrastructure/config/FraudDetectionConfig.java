package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.config;

import com.gogidix.aiservices.aifrauddetectionservice.domain.policy.FraudDetectionPolicy;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.MlModelPort;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.adapter.MlModelAdapter;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.InMemoryFraudRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for fraud detection service.
 */
@Configuration
public class FraudDetectionConfig {

    @Bean
    public InMemoryFraudRepository inMemoryFraudRepository() {
        return new InMemoryFraudRepository();
    }

    @Bean
    public MlModelPort mlModelPort() {
        return new MlModelAdapter();
    }

    @Bean
    public FraudDetectionPolicy fraudDetectionPolicy() {
        return new FraudDetectionPolicy();
    }
}
