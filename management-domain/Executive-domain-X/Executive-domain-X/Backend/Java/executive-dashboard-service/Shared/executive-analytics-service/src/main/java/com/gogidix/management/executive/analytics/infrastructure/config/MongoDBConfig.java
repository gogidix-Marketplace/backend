package com.gogidix.management.executive.analytics.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.EnableRetry;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableRetry
@Slf4j
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:management_executive}")
    private String database;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Value("${spring.data.mongodb.authentication-database:admin}")
    private String authDatabase;

    @Value("${spring.data.mongodb.auto-index-creation:true}")
    private boolean autoIndexCreation;

    @Value("${spring.data.mongodb.connection-timeout:10000}")
    private int connectionTimeout;

    @Value("${spring.data.mongodb.socket-timeout:30000}")
    private int socketTimeout;

    @Value("${spring.data.mongodb.max-connection-per-host:100}")
    private int maxConnectionPerHost;

    @Value("${spring.data.mongodb.min-connection-per-host:10}")
    private int minConnectionPerHost;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        log.info("Configuring MongoDB client for database: {}", database);
        ConnectionString connectionString;
        if (username != null && !username.isBlank()) {
            connectionString = new ConnectionString(
                String.format("mongodb://%s:%s@%s:%d/%s?authSource=%s",
                    username, password, host, port, database, authDatabase)
            );
        } else {
            connectionString = new ConnectionString(
                String.format("mongodb://%s:%d/%s", host, port, database)
            );
        }
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToSocketSettings(builder -> builder
                .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(socketTimeout, TimeUnit.MILLISECONDS)
            )
            .applyToConnectionPoolSettings(builder -> builder
                .maxSize(maxConnectionPerHost)
                .minSize(minConnectionPerHost)
                .maxWaitTime(connectionTimeout, TimeUnit.MILLISECONDS)
            )
            .applyToClusterSettings(builder -> builder
                .serverSelectionTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
            )
            .build();
        log.info("MongoDB client configured: host={}, port={}, database={}", host, port, database);
        try {
            MongoClient client = MongoClients.create(settings);
            log.info("MongoDB connection successful");
            return client;
        } catch (Exception e) {
            log.error("MongoDB connection failed: {}", e.getMessage());
            throw new DataAccessException("Failed to connect to MongoDB", e) {};
        }
    }

    @Bean
    public MongoTemplate mongoTemplate() throws DataAccessException {
        MongoTemplate template = new MongoTemplate(mongoClient(), getDatabaseName());
        log.info("MongoTemplate configured for database: {}", getDatabaseName());
        return template;
    }

    @Override
    public boolean autoIndexCreation() {
        return autoIndexCreation;
    }
}
