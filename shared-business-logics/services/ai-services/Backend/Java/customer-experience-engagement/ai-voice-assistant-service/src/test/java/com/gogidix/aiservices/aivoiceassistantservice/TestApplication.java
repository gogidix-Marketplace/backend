package com.gogidix.aiservices.aivoiceassistantservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(
    scanBasePackages = {
        "com.gogidix.aiservices.aivoiceassistantservice"
    },
    exclude = {MongoAutoConfiguration.class}
)
public class TestApplication {
}
