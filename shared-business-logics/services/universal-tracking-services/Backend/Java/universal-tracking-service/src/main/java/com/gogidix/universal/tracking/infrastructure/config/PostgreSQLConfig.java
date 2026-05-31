package com.gogidix.universal.tracking.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * PostgreSQL configuration for JPA repositories.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.gogidix.universal.tracking.infrastructure.persistence.postgres")
@EnableTransactionManagement
public class PostgreSQLConfig {
    // Configuration is handled via application.yml
}
