package com.gogidix.shared.warehousing.shelf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Shelf Management API",
        version = "1.0.0",
        description = "Multi-tenant shelf management service for warehouses"
    )
)
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.gogidix.shared.warehousing.shelf",
    "com.gogidix.shared.multitenancy"
})
@EnableMongoAuditing
@EnableKafka
public class ShelfApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShelfApplication.class, args);
    }
}
