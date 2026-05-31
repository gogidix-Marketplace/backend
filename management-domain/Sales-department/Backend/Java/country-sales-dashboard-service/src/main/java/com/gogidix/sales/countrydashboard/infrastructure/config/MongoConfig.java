package com.gogidix.sales.countrydashboard.infrastructure.config;

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
 * Configures MongoDB connection and settings
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.sales.countrydashboard.infrastructure.persistence.mongo")
@Slf4j
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:country-sales-dashboard-service_db}")
    private String database;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Value("${spring.data.mongodb.authentication-database:admin}")
    private String authenticationDatabase;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        StringBuilder connectionString = new StringBuilder("mongodb://");

        if (username != null && !username.isBlank()) {
            connectionString.append(username)
                    .append(":")
                    .append(password)
                    .append("@");
        }

        connectionString.append(host)
                .append(":")
                .append(port);

        if (username != null && !username.isBlank()) {
            connectionString.append("/?authSource=")
                    .append(authenticationDatabase);
        }

        log.info("Connecting to MongoDB at {}:{}", host, port);

        return MongoClients.create(connectionString.toString());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
