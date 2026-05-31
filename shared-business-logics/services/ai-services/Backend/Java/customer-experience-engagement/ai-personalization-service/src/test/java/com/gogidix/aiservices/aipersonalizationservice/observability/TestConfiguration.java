package com.gogidix.aiservices.aipersonalizationservice.observability;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(
    scanBasePackages = {
        "com.gogidix.aiservices.aipersonalizationservice.infrastructure.governance",
        "com.gogidix.aiservices.aipersonalizationservice.infrastructure.metrics"
    },
    exclude = {MongoAutoConfiguration.class}
)
public class TestConfiguration {
}
