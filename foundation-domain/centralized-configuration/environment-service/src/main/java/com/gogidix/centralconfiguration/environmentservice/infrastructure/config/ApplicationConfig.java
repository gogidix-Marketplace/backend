package com.gogidix.centralconfiguration.environmentservice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application configuration for environment service.
 */
@Configuration
@EnableJpaRepositories(basePackages = {
    "com.gogidix.centralconfiguration.environmentservice.infrastructure.persistence.postgres"
})
public class ApplicationConfig {
}
