package com.gogidix.courier.etaservice.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

/**
 * MongoDB configuration for ETA service.
 */
@Configuration
@EnableMongoRepositories(basePackages = {
        "com.gogidix.courier.etaservice.infrastructure.persistence.repository"
})
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:eta_service}")
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

    @Override
    public MongoClient mongoClient() {
        StringBuilder connectionString = new StringBuilder("mongodb://");

        if (username != null && !username.isBlank()) {
            connectionString.append(username).append(":").append(password).append("@");
        }

        connectionString.append(host).append(":").append(port);

        if (username != null && !username.isBlank()) {
            connectionString.append("/?authSource=").append(authenticationDatabase);
        }

        ConnectionString connString = new ConnectionString(connectionString.toString());

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .build();

        return MongoClients.create(settings);
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
