package com.gogidix.centralconfiguration.configserver.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * PostgreSQL configuration for config server.
 */
@Configuration
@EnableJpaRepositories(basePackages = {
    "com.gogidix.centralconfiguration.configserver.infrastructure.persistence.postgres"
})
public class PostgreSQLConfig {
}
