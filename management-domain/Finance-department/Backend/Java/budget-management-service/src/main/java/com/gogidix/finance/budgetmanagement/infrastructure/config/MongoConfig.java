package com.gogidix.finance.budgetmanagement.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.finance.budgetmanagement.domain.repository")
@EnableMongoAuditing
public class MongoConfig {
}
