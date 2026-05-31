package com.gogidix.aiservices.aisearchservice.governance;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * Minimal test configuration for governance tests.
 * Excludes MongoDB and only includes necessary beans.
 */
@SpringBootApplication(
    scanBasePackages = {
        "com.gogidix.aiservices.aisearchservice.infrastructure.governance",
        "com.gogidix.aiservices.aisearchservice.infrastructure.metrics"
    },
    exclude = {MongoAutoConfiguration.class}
)
public class TestConfiguration {
}
