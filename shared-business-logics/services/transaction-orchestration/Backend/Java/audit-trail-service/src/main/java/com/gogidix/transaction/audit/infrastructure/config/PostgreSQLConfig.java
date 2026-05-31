package com.gogidix.transaction.audit.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * PostgreSQL configuration for Audit Trail Service.
 */
@Configuration
@EnableJpaRepositories(basePackages = {
    "com.gogidix.transaction.audit.infrastructure.persistence.postgres"
})
@EnableJpaAuditing
public class PostgreSQLConfig {
    // JPA configuration is in application.yml
}
