package com.gogidix.shared.warehousing.inventory.analytics.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 *
 * Configures MongoDB repositories and auditing for inventory analytics service
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.warehousing.inventory.analytics.domain.repository")
@EnableMongoAuditing
public class MongoDBConfig {
}
