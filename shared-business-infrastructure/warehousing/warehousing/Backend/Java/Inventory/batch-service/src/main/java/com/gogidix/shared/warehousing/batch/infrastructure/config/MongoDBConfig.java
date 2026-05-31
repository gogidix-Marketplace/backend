package com.gogidix.shared.warehousing.batch.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.warehousing.batch.domain.repository")
@EnableMongoAuditing
public class MongoDBConfig {
}
