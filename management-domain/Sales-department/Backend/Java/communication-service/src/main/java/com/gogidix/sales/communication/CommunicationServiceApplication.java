package com.gogidix.sales.communication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Communication Service Application
 * Main Spring Boot application class for the Communication microservice
 *
 * This service provides multi-tenant communication capabilities including:
 * - Message management (send, receive, read/unread tracking)
 * - Conversation management
 * - Multiple communication channels (Email, SMS, In-App, WhatsApp)
 * - Message templates
 * - Attachment support
 * - Domain events via Kafka
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.sales.communication")
@EnableMongoAuditing
@EnableKafka
@EnableAsync
@EnableScheduling
public class CommunicationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunicationServiceApplication.class, args);
    }
}
