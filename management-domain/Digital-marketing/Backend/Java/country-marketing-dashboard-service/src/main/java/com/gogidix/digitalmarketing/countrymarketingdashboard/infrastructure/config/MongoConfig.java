package com.gogidix.digitalmarketing.countrymarketingdashboard.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.digitalmarketing.countrymarketingdashboard.domain.repository")
@EnableMongoAuditing
public class MongoConfig {
}
