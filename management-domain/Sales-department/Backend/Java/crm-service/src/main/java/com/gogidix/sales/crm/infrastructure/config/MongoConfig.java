package com.gogidix.sales.crm.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.util.concurrent.TimeUnit;

/**
 * MongoDB Configuration
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.sales.crm.domain.repository")
@Slf4j
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:crm-service_db}")
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
    @Bean
    public MongoClient mongoClient() {
        log.info("Configuring MongoDB client for host: {}, port: {}, database: {}", host, port, database);

        ConnectionString connectionString;
        if (username != null && !username.isEmpty()) {
            connectionString = new ConnectionString(String.format(
                "mongodb://%s:%s@%s:%d/%s?authSource=%s",
                username, password, host, port, database, authenticationDatabase
            ));
        } else {
            connectionString = new ConnectionString(String.format(
                "mongodb://%s:%d/%s",
                host, port, database
            ));
        }

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToConnectionPoolSettings(builder -> builder
                .maxConnectionIdleTime(60000, TimeUnit.MILLISECONDS)
                .maxSize(100)
                .minSize(10)
            )
            .applyToSocketSettings(builder -> builder
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
            )
            .applyToServerSettings(builder -> builder
                .heartbeatFrequency(10000, TimeUnit.MILLISECONDS)
            )
            .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory(MongoTemplate mongoTemplate) {
        return new MongoRepositoryFactory(mongoTemplate);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoTemplate mongoTemplate) {
        return (MappingMongoConverter) mongoTemplate.getConverter();
    }
}
