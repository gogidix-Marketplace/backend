package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration for authentication service.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.infrastructure.services.security.auth.infrastructure.persistence")
@EnableMongoAuditing
public class MongoConfig {
}
