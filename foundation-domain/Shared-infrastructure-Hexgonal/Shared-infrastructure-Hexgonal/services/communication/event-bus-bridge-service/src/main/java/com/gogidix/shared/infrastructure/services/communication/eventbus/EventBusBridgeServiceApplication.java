package com.gogidix.shared.infrastructure.services.communication.eventbus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
/**
 * Event Bus Bridge Service Application
 * Provides bridging between different event bus systems (Kafka, RabbitMQ) with multi-tenant MongoDB support.
 */
@SpringBootApplication
@EnableKafka
public class EventBusBridgeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventBusBridgeServiceApplication.class, args);
    }
}
