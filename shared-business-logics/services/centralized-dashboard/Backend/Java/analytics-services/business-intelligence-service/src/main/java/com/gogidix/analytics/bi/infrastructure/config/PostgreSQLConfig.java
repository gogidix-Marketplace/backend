package com.gogidix.analytics.bi.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * PostgreSQL configuration for Business Intelligence Service.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.gogidix.analytics.bi.domain.repository")
public class PostgreSQLConfig {
}
