package com.gogidix.centralconfiguration.configauditservice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application configuration for config audit service.
 */
@Configuration
@EnableJpaRepositories(basePackages = {
    "com.gogidix.centralconfiguration.configauditservice.infrastructure.persistence.postgres"
})
public class ApplicationConfig {
}
