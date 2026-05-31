package com.gogidix.analytics.metrics.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * PostgreSQL configuration for Metrics Aggregation Service.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.gogidix.analytics.metrics.domain.repository")
public class PostgreSQLConfig {
}
