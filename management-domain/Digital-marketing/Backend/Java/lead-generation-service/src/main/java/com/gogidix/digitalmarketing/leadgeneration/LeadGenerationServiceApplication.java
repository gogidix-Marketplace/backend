package com.gogidix.digitalmarketing.leadgeneration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
    "com.gogidix.digitalmarketing.leadgeneration",
    "com.gogidix.digitalmarketing.shared"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
@EnableKafka
public class LeadGenerationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeadGenerationServiceApplication.class, args);
    }
}
