package com.gogidix.transaction.monitoring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * PostgreSQL JPA Configuration for Transaction Monitoring Service.
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class PostgreSQLConfig {
}
