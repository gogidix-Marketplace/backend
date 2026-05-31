package com.gogidix.finance.accountspayable;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Abstract base class for MongoDB Testcontainers configuration.
 * Provides a shared MongoDB container for all integration tests.
 */
@Testcontainers
public abstract class AbstractMongoDBTestContainer {

    @Container
    protected static final MongoDBContainer mongoDBContainer = new MongoDBContainer(
            DockerImageName.parse("mongo:6.0"))
            .withReuse(true);

    @DynamicPropertySource
    protected static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeAll
    protected static void setUp() {
        // Ensure container is started
        if (!mongoDBContainer.isRunning()) {
            mongoDBContainer.start();
        }
    }
}
