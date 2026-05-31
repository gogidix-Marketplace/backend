package com.gogidix.shared.courier.driver.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.concurrent.TimeUnit;

/**
 * MongoDB Configuration for Driver Pool Service
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.courier.driver.domain.repository")
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString cs = new ConnectionString(mongoUri);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(cs)
                .applyToConnectionPoolSettings(builder ->
                        builder.maxConnectionIdleTime(60000, TimeUnit.MILLISECONDS)
                                .maxSize(50)
                                .minSize(5)
                )
                .applyToSocketSettings(builder ->
                        builder.connectTimeout(5000, TimeUnit.MILLISECONDS)
                                .readTimeout(30000, TimeUnit.MILLISECONDS)
                )
                .build();

        return MongoClients.create(settings);
    }
}
