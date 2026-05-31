package com.gogidix.shared.infrastructure.services.infrastructure.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Message Broker Service Application.
 * Provides messaging capabilities using Kafka as the underlying broker.
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.gogidix.shared.infrastructure.services.infrastructure.messaging")
public class MessageBrokerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageBrokerServiceApplication.class, args);
    }
}
