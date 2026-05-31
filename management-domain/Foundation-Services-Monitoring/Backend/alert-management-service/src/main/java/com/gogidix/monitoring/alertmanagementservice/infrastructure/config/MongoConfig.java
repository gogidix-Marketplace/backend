package com.gogidix.monitoring.alertmanagementservice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.monitoring.alertmanagementservice.domain.repository")
@EnableMongoAuditing
public class MongoConfig {
}
