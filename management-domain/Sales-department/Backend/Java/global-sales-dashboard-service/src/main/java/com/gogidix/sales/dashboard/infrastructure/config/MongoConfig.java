package com.gogidix.sales.dashboard.infrastructure.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 * Configures MongoDB for dashboard data persistence
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.sales.dashboard.infrastructure.persistence.mongo")
@Slf4j
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port:27017}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database:global-sales-dashboard-service_db}")
    private String mongoDatabase;

    @Value("${spring.data.mongodb.username:}")
    private String mongoUsername;

    @Value("${spring.data.mongodb.password:}")
    private String mongoPassword;

    @Override
    protected String getDatabaseName() {
        return mongoDatabase;
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        StringBuilder connectionString = new StringBuilder("mongodb://");

        if (mongoUsername != null && !mongoUsername.isBlank()) {
            connectionString.append(mongoUsername);
            if (mongoPassword != null && !mongoPassword.isBlank()) {
                connectionString.append(":").append(mongoPassword);
            }
            connectionString.append("@");
        }

        connectionString.append(mongoHost).append(":").append(mongoPort);
        connectionString.append("/").append(mongoDatabase);

        log.info("Connecting to MongoDB at {}:{}", mongoHost, mongoPort);

        return MongoClients.create(connectionString.toString());
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
