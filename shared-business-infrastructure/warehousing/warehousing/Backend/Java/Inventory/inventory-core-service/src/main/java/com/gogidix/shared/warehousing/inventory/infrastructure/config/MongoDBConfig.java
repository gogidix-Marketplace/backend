package com.gogidix.shared.warehousing.inventory.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 *
 * Configures MongoDB repositories and auditing
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.warehousing.inventory.domain.repository")
@EnableMongoAuditing
public class MongoDBConfig {
}
