package com.gogidix.shared.courier.ecommerce.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.courier.ecommerce.infrastructure.persistence.repository")
public class MongoDBConfig {
}
