package com.gogidix.shared.warehousing.reorder;

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
 * Reorder Service Application
 *
 * Multi-tenant reorder point management with MongoDB
 * Supports: E-commerce vendors, Logistics, Procurement, Public marketplace
 * Tracks reorder points and generates purchase orders
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gogidix.shared.warehousing.reorder",
    "com.gogidix.shared.multitenancy"
})
@EnableMongoAuditing
@EnableKafka
@EnableScheduling
@OpenAPIDefinition(
    info = @Info(
        title = "Reorder Service API",
        version = "1.0.0",
        description = "Multi-tenant API for automated reorder point management",
        contact = @Contact(name = "Gogidix Team", email = "support@gogidix.com"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
    )
)
public class ReorderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReorderServiceApplication.class, args);
    }
}
