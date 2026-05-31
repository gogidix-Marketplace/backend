package com.gogidix.platform.subscription.infrastructure.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA configuration for subscription service.
 * Registers attribute converters for JSON field mapping.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.gogidix.platform.subscription.infrastructure.persistence.postgres")
public class JpaConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public jakarta.persistence.AttributeConverter<?, ?>[] converters() {
        return new jakarta.persistence.AttributeConverter[]{
            new JsonMapConverter(),
            new JsonListMapConverter()
        };
    }
}
