package com.gogidix.shared.warehousing.warehouseconfig.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.warehousing.warehouseconfig.domain.repository")
@EnableMongoAuditing
public class MongoDBConfig {
}
