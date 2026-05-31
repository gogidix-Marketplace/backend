package com.gogidix.digitalmarketing.socialmedia.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration for Social Media Service
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.digitalmarketing.socialmedia.domain.repository")
@EnableMongoAuditing
public class MongoConfig {

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017/social_media}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database:social_media}")
    private String databaseName;

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), databaseName);
    }
}
