package com.gogidix.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

/**
 * Shared Testing Application for the Gogidix Social E-commerce Ecosystem.
 * Provides testing utilities, test containers, and testing frameworks.
 * 
 * Features:
 * - Testcontainers integration for PostgreSQL, Kafka, RabbitMQ, Redis, Elasticsearch
 * - WireMock for API mocking and testing
 * - Spring Boot test utilities and configuration
 * - Security testing components
 * - Database testing with H2 and JPA
 * - Kafka testing utilities
 * - OpenAPI testing support
 * - Performance and load testing utilities
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 * @version 1.0.0
 */
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
public class SharedTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedTestingApplication.class, args);
    }
}