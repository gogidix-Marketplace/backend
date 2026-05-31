package com.gogidix.courier.availabilityservice.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.concurrent.TimeUnit;

/**
 * MongoDB configuration for availability service.
 */
@Configuration
@EnableMongoRepositories(basePackages = {
        "com.gogidix.courier.availabilityservice.infrastructure.persistence.repository"
})
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:driver_availability}")
    private String database;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Value("${spring.data.mongodb.authentication-database:admin}")
    private String authDatabase;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString;
        if (username != null && !username.isBlank()) {
            connectionString = new ConnectionString(String.format(
                    "mongodb://%s:%s@%s:%d/%s?authSource=%s",
                    username, password, host, port, database, authDatabase
            ));
        } else {
            connectionString = new ConnectionString(String.format(
                    "mongodb://%s:%d/%s",
                    host, port, database
            ));
        }

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSocketSettings(builder -> builder
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS))
                .applyToClusterSettings(builder -> builder
                        .serverSelectionTimeout(10, TimeUnit.SECONDS))
                .build();

        return MongoClients.create(settings);
    }
}
