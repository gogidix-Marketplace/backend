package com.gogidix.shared.warehousing.shipping;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Shipping Service Application
 *
 * Multi-tenant shipping management with carrier integration
 * Supports: E-commerce vendors, Logistics, Procurement, Public marketplace
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gogidix.shared.warehousing.shipping"
})
@EnableMongoAuditing
@EnableKafka
@OpenAPIDefinition(
    info = @Info(
        title = "Shipping Service API",
        version = "1.0.0",
        description = "Multi-tenant shipping service with carrier integration and label generation",
        contact = @Contact(name = "Gogidix Team", email = "support@gogidix.com")
    )
)
public class ShippingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShippingApplication.class, args);
    }
}
