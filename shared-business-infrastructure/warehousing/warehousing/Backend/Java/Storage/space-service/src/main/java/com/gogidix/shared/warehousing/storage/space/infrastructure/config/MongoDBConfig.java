package com.gogidix.shared.warehousing.storage.space.infrastructure.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration for Space Service
 * Configures connection settings and repository scanning
 */
@Slf4j
@Configuration
@EnableMongoRepositories(
        basePackages = "com.gogidix.shared.warehousing.storage.space.domain.repository"
)
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database:shared_warehousing}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    public MongoClient mongoClient() {
        log.info("Connecting to MongoDB at: {}", mongoUri.replaceAll("://.*:.*@", "://***:***@"));
        return MongoClients.create(mongoUri);
    }
}
