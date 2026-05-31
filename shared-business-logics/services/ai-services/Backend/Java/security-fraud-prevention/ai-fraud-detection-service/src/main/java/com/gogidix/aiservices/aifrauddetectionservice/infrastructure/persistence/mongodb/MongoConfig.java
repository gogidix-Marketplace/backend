package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration for the fraud detection service.
 * Configures MongoDB client, template, and repository scanning.
 */
@Configuration
@EnableMongoRepositories(basePackages = {
    "com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb"
})
public class MongoConfig extends AbstractMongoClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:ai-fraud-detection}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        String connectionString = String.format("mongodb://%s:%d", host, port);
        log.info("Creating MongoDB client with connection: {}", connectionString);
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    /**
     * Event listener for logging MongoDB operations.
     */
    @Bean
    public LoggingEventListener mongoLoggingEventListener() {
        return new LoggingEventListener();
    }
}
