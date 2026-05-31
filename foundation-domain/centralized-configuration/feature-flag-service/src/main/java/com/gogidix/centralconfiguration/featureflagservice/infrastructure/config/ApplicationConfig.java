package com.gogidix.centralconfiguration.featureflagservice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application configuration for feature flag service.
 */
@Configuration
@EnableJpaRepositories(basePackages = {
    "com.gogidix.centralconfiguration.featureflagservice.infrastructure.persistence.postgres"
})
public class ApplicationConfig {
}
