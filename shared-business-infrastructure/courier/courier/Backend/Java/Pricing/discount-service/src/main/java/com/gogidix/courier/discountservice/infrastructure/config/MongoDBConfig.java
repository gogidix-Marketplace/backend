package com.gogidix.courier.discountservice.infrastructure.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.courier.discountservice.infrastructure.persistence.repository")
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.database}")
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
        String connectionString = buildConnectionString();
        return MongoClients.create(connectionString);
    }

    private String buildConnectionString() {
        StringBuilder sb = new StringBuilder("mongodb://");

        if (username != null && !username.isEmpty()) {
            sb.append(username)
                    .append(":")
                    .append(password)
                    .append("@");
        }

        sb.append(host)
                .append(":")
                .append(port)
                .append("/");

        if (username != null && !username.isEmpty()) {
            sb.append("?authSource=")
                    .append(authenticationDatabase);
        }

        return sb.toString();
    }
}
