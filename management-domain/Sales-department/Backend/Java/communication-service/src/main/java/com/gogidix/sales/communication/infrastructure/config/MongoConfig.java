package com.gogidix.sales.communication.infrastructure.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.sales.communication.infrastructure.persistence.mongo")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:communication-service_db}")
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
        String connectionString = buildConnectionString();
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    private String buildConnectionString() {
        StringBuilder sb = new StringBuilder("mongodb://");

        if (username != null && !username.isEmpty()) {
            sb.append(username)
                    .append(":")
                    .append(password != null ? password : "")
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
