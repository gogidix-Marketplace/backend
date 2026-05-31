package com.gogidix.shared.warehousing.serialization;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Serialization Service Application
 *
 * Multi-tenant serialized item tracking with MongoDB
 * Supports: E-commerce vendors, Logistics, Procurement, Public marketplace
 * Tracks individual serialized items, batches, and expiry dates
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gogidix.shared.warehousing.serialization",
    "com.gogidix.shared.multitenancy"
})
@EnableMongoAuditing
@EnableKafka
@OpenAPIDefinition(
    info = @Info(
        title = "Serialization Service API",
        version = "1.0.0",
        description = "Multi-tenant API for tracking individual serialized items, batches, and expiry dates",
        contact = @Contact(name = "Gogidix Team", email = "support@gogidix.com"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
    )
)
public class SerializationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SerializationServiceApplication.class, args);
    }
}
