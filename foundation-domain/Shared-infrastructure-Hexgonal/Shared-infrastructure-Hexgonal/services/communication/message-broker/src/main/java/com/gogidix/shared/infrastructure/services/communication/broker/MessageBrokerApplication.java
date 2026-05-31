package com.gogidix.shared.infrastructure.services.communication.broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = {
        "com.gogidix.shared.infrastructure.services.communication.broker",
        "com.gogidix.shared.infrastructure.core.tenancy",
        "com.gogidix.shared"
})
@EnableDiscoveryClient
@EnableMongoAuditing
@EnableKafka
public class MessageBrokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageBrokerApplication.class, args);
    }
}
