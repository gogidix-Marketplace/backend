package com.gogidix.centralconfiguration.notificationservice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application configuration for notification service.
 */
@Configuration
@EnableJpaRepositories(basePackages = {
    "com.gogidix.centralconfiguration.notificationservice.infrastructure.persistence.postgres"
})
public class ApplicationConfig {
}
