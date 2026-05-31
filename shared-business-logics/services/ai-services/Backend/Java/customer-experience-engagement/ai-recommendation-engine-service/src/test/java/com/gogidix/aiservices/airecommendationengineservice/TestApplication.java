package com.gogidix.aiservices.airecommendationengineservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(
    scanBasePackages = {
        "com.gogidix.aiservices.airecommendationengineservice"
    },
    exclude = {MongoAutoConfiguration.class}
)
public class TestApplication {
}
