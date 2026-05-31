package com.gogidix.shared.infrastructure.services.communication.webhook.infrastructure.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
@Configuration
@EnableMongoRepositories(basePackages = {
    "com.gogidix.shared.infrastructure.services.webhookmanagement.infrastructure.persistence"
})
@EnableMongoAuditing
public class MongoConfig {
    // MongoDB configuration
}
