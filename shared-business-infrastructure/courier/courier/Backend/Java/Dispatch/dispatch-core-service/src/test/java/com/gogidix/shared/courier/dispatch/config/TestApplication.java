package com.gogidix.shared.courier.dispatch.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Test Application Configuration
 * Uses minimal setup for unit tests
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.gogidix.shared.courier.dispatch.application",
                "com.gogidix.shared.courier.dispatch.domain",
                "com.gogidix.shared.courier.dispatch.interfaces"
        }
)
@EnableMongoRepositories(
        basePackages = "com.gogidix.shared.courier.dispatch.domain.repository"
)
public class TestApplication {
}
