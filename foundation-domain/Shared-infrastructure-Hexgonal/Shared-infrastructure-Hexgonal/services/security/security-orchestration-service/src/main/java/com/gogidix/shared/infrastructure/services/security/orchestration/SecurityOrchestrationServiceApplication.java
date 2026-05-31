package com.gogidix.shared.infrastructure.services.security.orchestration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class SecurityOrchestrationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityOrchestrationServiceApplication.class, args);
    }
}
