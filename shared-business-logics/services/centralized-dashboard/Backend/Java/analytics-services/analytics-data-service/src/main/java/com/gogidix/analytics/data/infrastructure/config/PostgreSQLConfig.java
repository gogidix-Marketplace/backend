package com.gogidix.analytics.data.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * PostgreSQL configuration for Analytics Data Service.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.gogidix.analytics.data.domain.repository")
public class PostgreSQLConfig {
}
