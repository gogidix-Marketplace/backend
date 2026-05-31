package com.gogidix.corporatecms.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.corporatecms.domain.repository")
@EnableMongoAuditing(auditorAwareRef = "auditorProvider")
public class MongoConfig {
}
