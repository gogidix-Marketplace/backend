package com.gogidix.hr.employeeselfservice.infrastructure.config;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Abstract base class for MongoDB integration tests using Testcontainers
 */
@DataMongoTest
@Testcontainers
public abstract class AbstractMongoDBTestContainer {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(
        DockerImageName.parse("mongo:6.0.4"))
        .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.auto-index-creation", () -> true);
    }

    @BeforeAll
    static void setUp() {
        mongoDBContainer.start();
    }
}
