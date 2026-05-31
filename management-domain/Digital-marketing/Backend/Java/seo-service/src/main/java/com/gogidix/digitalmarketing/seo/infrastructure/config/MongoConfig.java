package com.gogidix.digitalmarketing.seo.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.digitalmarketing.seo.domain.repository")
@EnableMongoAuditing
public class MongoConfig {
}
