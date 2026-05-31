package com.gogidix.transaction.progress.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * PostgreSQL JPA Configuration for Progress Step Service.
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class PostgreSQLConfig {
}
