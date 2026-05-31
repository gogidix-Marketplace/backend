package com.gogidix.sales.forecast.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * MongoDB Configuration
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.sales.forecast.infrastructure.persistence")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:forecast-management-service_db}")
    private String database;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {
        String connectionString = buildConnectionString();
        return MongoClients.create(connectionString);
    }

    private String buildConnectionString() {
        StringBuilder sb = new StringBuilder("mongodb://");

        if (username != null && !username.isBlank()) {
            sb.append(username).append(":").append(password).append("@");
        }

        sb.append(host).append(":").append(port).append("/").append(database);

        return sb.toString();
    }
}
