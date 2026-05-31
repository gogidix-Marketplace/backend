package com.gogidix.globalbusinessmanagement.multicurrency.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

/**
 * MongoDB configuration for Multi-Currency Service.
 */
@Slf4j
@Configuration
@EnableMongoRepositories(basePackages = {
    "com.gogidix.globalbusinessmanagement.multicurrency.domain.repository"
})
public class MultiCurrencyMongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:multi_currency_service_db}")
    private String database;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Value("${spring.data.mongodb.authentication-database:admin}")
    private String authDatabase;

    @Value("${spring.data.mongodb.uuid-representation:standard}")
    private String uuidRepresentation;

    @Value("${spring.data.mongodb.auto-index-creation:true}")
    private boolean autoIndexCreation;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        log.info("Configuring MongoDB client for Multi-Currency Service: {}:{}", host, port);

        ConnectionString connectionString;
        if (username != null && !username.isEmpty()) {
            connectionString = new ConnectionString(String.format(
                "mongodb://%s:%s@%s:%d/%s?authSource=%s&uuidRepresentation=%s",
                username, password, host, port, database, authDatabase, uuidRepresentation));
        } else {
            connectionString = new ConnectionString(String.format(
                "mongodb://%s:%d/%s?uuidRepresentation=%s",
                host, port, database, uuidRepresentation));
        }

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

        return MongoClients.create(settings);
    }

    @Bean
    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Collections.emptyList());
    }

    @Bean
    public boolean autoIndexCreation() {
        return autoIndexCreation;
    }
}
