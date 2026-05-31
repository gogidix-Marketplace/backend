package com.gogidix.courier.assignmentservice.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * MongoDB configuration for assignment service.
 */
@Configuration
@EnableMongoAuditing
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database:assignment_service}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        builder.applyConnectionString(connectionString);
    }
}
