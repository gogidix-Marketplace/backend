package com.gogidix.globalbusiness.kafkaingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Main application class for Kafka Ingestion Service.
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.globalbusiness.kafkaingestion"
})
@EnableMongoAuditing
@EnableKafka
public class KafkaIngestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaIngestionApplication.class, args);
    }
}
