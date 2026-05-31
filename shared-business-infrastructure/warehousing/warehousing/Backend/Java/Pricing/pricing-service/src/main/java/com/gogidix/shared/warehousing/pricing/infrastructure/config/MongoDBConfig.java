package com.gogidix.shared.warehousing.pricing.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 *
 * Configures MongoDB repositories and auditing for pricing service
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.warehousing.pricing.domain.repository")
@EnableMongoAuditing
public class MongoDBConfig {
}
