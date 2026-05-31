package com.gogidix.customersupport.notification.infrastructure.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;

/**
 * MongoDB Configuration for Notification Service
 */
@Configuration
@EnableMongoRepositories(basePackages = {
        "com.gogidix.customersupport.notification.domain.repository"
})
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
public class MongoConfig {

    @Value("${spring.data.mongodb.host:localhost}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port:27017}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database:notification_db}")
    private String databaseName;

    /**
     * Configure MongoDB Client
     */
    @Bean
    public MongoClient mongoClient() {
        String connectionString = String.format("mongodb://%s:%d", mongoHost, mongoPort);
        return MongoClients.create(connectionString);
    }

    /**
     * Configure MongoTemplate
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, databaseName);
    }

    /**
     * Auditor aware for tracking who created/modified entities
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("system");
    }
}
