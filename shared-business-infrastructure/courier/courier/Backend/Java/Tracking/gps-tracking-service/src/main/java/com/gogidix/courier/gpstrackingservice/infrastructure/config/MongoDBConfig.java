package com.gogidix.courier.gpstrackingservice.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration for GPS Tracking Service.
 */
@Configuration
@EnableMongoRepositories(basePackages =
        "com.gogidix.courier.gpstrackingservice.infrastructure.persistence.repository")
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:gps_tracking}")
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
    @Bean
    public MongoClient mongoClient() {
        StringBuilder connectionString = new StringBuilder("mongodb://");

        if (username != null && !username.isEmpty()) {
            connectionString.append(username)
                    .append(":")
                    .append(password != null ? password : "")
                    .append("@");
        }

        connectionString.append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(database);

        if (username != null && !username.isEmpty()) {
            connectionString.append("?authSource=").append(authDatabase);
        }

        ConnectionString cs = new ConnectionString(connectionString.toString());
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(cs)
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
