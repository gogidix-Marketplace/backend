package com.gogidix.marketing.campaign.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@EnableMongoRepositories(basePackages = {
    "com.gogidix.marketing.campaign.domain.repository",
    "com.gogidix.digitalmarketing.campaignmanagement.domain.repository"
})
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MongoDBConfig.class);

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:digital_marketing_campaigns}")
    private String database;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Value("${spring.data.mongodb.authentication-database:}")
    private String authDatabase;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        log.info("Creating MongoDB client for database: {}", database);

        ConnectionString connectionString;
        if (username != null && !username.isEmpty()) {
            String authDb = authDatabase != null && !authDatabase.isEmpty() ? authDatabase : "admin";
            connectionString = new ConnectionString(
                String.format("mongodb://%s:%s@%s:%d/%s?authSource=%s",
                    username, password, host, port, database, authDb)
            );
        } else {
            connectionString = new ConnectionString(
                String.format("mongodb://%s:%d/%s", host, port, database)
            );
        }

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToSocketSettings(builder -> builder
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS))
            .applyToConnectionPoolSettings(builder -> builder
                .maxConnectionIdleTime(60000, TimeUnit.MILLISECONDS)
                .maxSize(50)
                .minSize(5))
            .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
