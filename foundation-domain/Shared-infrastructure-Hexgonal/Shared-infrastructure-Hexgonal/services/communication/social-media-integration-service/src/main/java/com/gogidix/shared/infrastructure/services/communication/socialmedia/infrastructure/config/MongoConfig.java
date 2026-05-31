package com.gogidix.shared.infrastructure.services.communication.socialmedia.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {
    "com.gogidix.sharedinfrastructure.socialmedia"
})
@EnableMongoAuditing
public class MongoConfig {
    // MongoDB configuration
}
