package com.gogidix.shared.warehousing.expiration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Expiration Service Application
 *
 * Multi-tenant expiration tracking with MongoDB
 * Supports: E-commerce vendors, Logistics, Procurement, Public marketplace
 * Tracks expiration dates and generates alerts
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gogidix.shared.warehousing.expiration",
    "com.gogidix.shared.multitenancy"
})
@EnableMongoAuditing
@EnableKafka
@EnableScheduling
@OpenAPIDefinition(
    info = @Info(
        title = "Expiration Service API",
        version = "1.0.0",
        description = "Multi-tenant API for tracking and alerting on expiration dates",
        contact = @Contact(name = "Gogidix Team", email = "support@gogidix.com"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
    )
)
public class ExpirationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpirationServiceApplication.class, args);
    }
}
