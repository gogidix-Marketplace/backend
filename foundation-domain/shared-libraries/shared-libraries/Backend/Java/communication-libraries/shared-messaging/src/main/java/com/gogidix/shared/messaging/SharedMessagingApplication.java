package com.gogidix.shared.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Gogidix Shared Messaging Library Application
 * 
 * Standardized hexagonal architecture implementation providing unified messaging 
 * and event-driven communication capabilities for the Gogidix Social E-commerce Ecosystem.
 * 
 * Features:
 * - Kafka message publishing and consuming
 * - RabbitMQ AMQP messaging
 * - Redis pub/sub messaging
 * - WebSocket real-time communication
 * - Event sourcing patterns
 * - Message routing and transformation
 * - Eureka service discovery integration
 * 
 * @author Gogidix Development Team
 * @version 2.0.0
 * @since 2024-01-01
 */
@SpringBootApplication
public class SharedMessagingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedMessagingApplication.class, args);
    }
}
