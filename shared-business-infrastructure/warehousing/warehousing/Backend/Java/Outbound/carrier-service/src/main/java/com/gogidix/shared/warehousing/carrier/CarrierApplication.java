package com.gogidix.shared.warehousing.carrier;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"com.gogidix.shared.warehousing.carrier"})
@EnableMongoAuditing
@OpenAPIDefinition(
    info = @Info(
        title = "Carrier Service API",
        version = "1.0.0",
        description = "Multi-tenant carrier management and selection service",
        contact = @Contact(name = "Gogidix Team", email = "support@gogidix.com")
    )
)
public class CarrierApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarrierApplication.class, args);
    }
}
