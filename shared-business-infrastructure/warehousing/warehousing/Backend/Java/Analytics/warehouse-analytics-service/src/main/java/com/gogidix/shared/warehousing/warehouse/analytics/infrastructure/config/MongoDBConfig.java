package com.gogidix.shared.warehousing.warehouse.analytics.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 *
 * Configures MongoDB repositories and auditing
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.warehousing.warehouse.analytics.domain.repository")
@EnableMongoAuditing
public class MongoDBConfig {
}
