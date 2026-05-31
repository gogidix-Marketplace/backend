package com.gogidix.shared.infrastructure.services.security.dlp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class DlpServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DlpServiceApplication.class, args);
    }
}
