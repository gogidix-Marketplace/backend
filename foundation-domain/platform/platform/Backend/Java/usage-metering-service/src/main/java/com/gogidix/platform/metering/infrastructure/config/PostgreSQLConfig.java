package com.gogidix.platform.metering.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * PostgreSQL configuration for Usage Metering Service.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.gogidix.platform.metering.domain.repository")
@EnableTransactionManagement
public class PostgreSQLConfig {
    // Additional PostgreSQL configuration can be added here
}
