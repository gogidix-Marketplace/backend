package com.gogidix.shared.infrastructure.services.security.dlp.test.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Base class for integration tests with Testcontainers.
 * Containers are started once before all tests in the class.
 *
 * Note: Spring Boot application context is NOT loaded by default
 * to avoid circular dependency with container startup.
 */
public abstract class AbstractIntegrationTest {

    static MongoDBContainer mongoDBContainer;
    static KafkaContainer kafkaContainer;
    static boolean containersAvailable = false;

    @BeforeAll
    static void setUp() {
        try {
            mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0.13"));
            mongoDBContainer.start();

            kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
            kafkaContainer.start();

            containersAvailable = true;
            System.out.println("✅ Testcontainers started: MongoDB and Kafka available");
        } catch (Exception e) {
            containersAvailable = false;
            System.err.println("⚠️  Testcontainers not available - running without containers: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDown() {
        if (mongoDBContainer != null && mongoDBContainer.isRunning()) {
            mongoDBContainer.stop();
        }
        if (kafkaContainer != null && kafkaContainer.isRunning()) {
            kafkaContainer.stop();
        }
    }

    /**
     * Check if containers are available for tests that need them.
     */
    protected boolean areContainersAvailable() {
        return containersAvailable;
    }
}
