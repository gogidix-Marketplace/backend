package com.gogidix.digitalmarketing.brandmanagement.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.digitalmarketing.brandmanagement.domain.repository")
@EnableMongoAuditing
public class MongoConfig {
}
