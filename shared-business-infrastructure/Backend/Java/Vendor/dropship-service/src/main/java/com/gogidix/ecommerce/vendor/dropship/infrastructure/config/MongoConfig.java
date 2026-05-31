package com.gogidix.ecommerce.vendor.dropship.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.vendor.dropship.domain.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override protected String getDatabaseName() { return "gogidix_dropship"; }
    @Bean @Override public MongoClient mongoClient() {
        ConnectionString cs = new ConnectionString("mongodb://localhost:27017");
        return MongoClients.create(MongoClientSettings.builder().applyConnectionString(cs).build());
    }
}