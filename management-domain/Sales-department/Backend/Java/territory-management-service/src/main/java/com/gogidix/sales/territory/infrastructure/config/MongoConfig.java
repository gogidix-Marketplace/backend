package com.gogidix.sales.territory.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.concurrent.TimeUnit;

/**
 * MongoDB Configuration
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.sales.territory.infrastructure.persistence.mongo")
@Slf4j
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:territory-management-service_db}")
    private String database;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString;
        if (username != null && !username.isEmpty()) {
            connectionString = new ConnectionString(
                String.format("mongodb://%s:%s@%s:%d/%s?authSource=admin",
                    username, password, host, port, database)
            );
        } else {
            connectionString = new ConnectionString(
                String.format("mongodb://%s:%d/%s", host, port, database)
            );
        }

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToSocketSettings(builder ->
                builder.connectTimeout(5000, TimeUnit.MILLISECONDS)
            )
            .applyToServerSettings(builder ->
                builder.heartbeatFrequency(10000, TimeUnit.MILLISECONDS)
            )
            .build();

        log.info("Connecting to MongoDB at {}:{}", host, port);
        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
