package com.gogidix.sales.dealmanagement.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.sales.dealmanagement.infrastructure.persistence.mongo")
@EnableMongoAuditing
public class MongoConfig {
}
