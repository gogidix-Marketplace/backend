package com.gogidix.courier.tenantservice.infrastructure.config;

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
 * MongoDB configuration for tenant service.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.courier.tenantservice.infrastructure.persistence.repository")
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.username:}")
    private String username;

    @Value("${spring.data.mongodb.password:}")
    private String password;

    @Value("${spring.data.mongodb.authentication-database:admin}")
    private String authDatabase;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient() {
        StringBuilder connectionString = new StringBuilder("mongodb://");

        if (username != null && !username.isBlank()) {
            connectionString.append(username)
                    .append(":")
                    .append(password)
                    .append("@");
        }

        connectionString.append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(databaseName);

        if (username != null && !username.isBlank()) {
            connectionString.append("?authSource=")
                    .append(authDatabase);
        }

        ConnectionString cs = new ConnectionString(connectionString.toString());

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
