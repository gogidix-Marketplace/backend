package com.gogidix.shared.warehousing.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "E-Commerce Warehouse Integration API",
        version = "1.0.0",
        description = "E-commerce fulfillment integration for zone-based warehouse operations"
    )
)
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class EcommerceWarehouseApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceWarehouseApplication.class, args);
    }
}
