package com.gogidix.sales.analytics.infrastructure.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration for Sales Analytics Service
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.sales.analytics.infrastructure.persistence.mongo")
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient() {
        StringBuilder connectionString = new StringBuilder("mongodb://");

        if (username != null && !username.isEmpty()) {
            connectionString.append(username)
                    .append(":")
                    .append(password)
                    .append("@");
        }

        connectionString.append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(databaseName);

        if (username != null && !username.isEmpty()) {
            connectionString.append("?authSource=admin");
        }

        return MongoClients.create(connectionString.toString());
    }
}
