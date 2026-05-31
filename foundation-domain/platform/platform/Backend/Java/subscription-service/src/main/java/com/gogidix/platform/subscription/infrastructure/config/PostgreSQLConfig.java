package com.gogidix.platform.subscription.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * PostgreSQL configuration for Subscription Service.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.gogidix.platform.subscription.domain.repository")
public class PostgreSQLConfig {
}
