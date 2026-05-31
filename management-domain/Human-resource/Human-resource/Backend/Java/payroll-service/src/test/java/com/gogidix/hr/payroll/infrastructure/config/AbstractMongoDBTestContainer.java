package com.gogidix.hr.payroll.infrastructure.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;

/**
 * Abstract base class for MongoDB Testcontainers integration
 * Provides a shared MongoDB container for all integration tests
 */
public abstract class AbstractMongoDBTestContainer {

    protected static final String DATABASE_NAME = "management_db";

    protected static MongoDBContainer mongoDBContainer;

    /**
     * Starts the MongoDB container before all tests
     */
    @BeforeAll
    public static void startMongoDBContainer() {
        if (mongoDBContainer == null || !mongoDBContainer.isRunning()) {
            mongoDBContainer = new MongoDBContainer("mongo:6.0");
            mongoDBContainer.start();
            System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl() + "?" + DATABASE_NAME);
        }
    }

    /**
     * Creates a MongoTemplate instance for testing
     */
    protected MongoTemplate createMongoTemplate() {
        MongoClient mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
        return new MongoTemplate(mongoClient, DATABASE_NAME);
    }

    /**
     * Gets the MongoDB connection string
     */
    protected static String getMongoConnectionString() {
        return mongoDBContainer.getReplicaSetUrl();
    }
}
