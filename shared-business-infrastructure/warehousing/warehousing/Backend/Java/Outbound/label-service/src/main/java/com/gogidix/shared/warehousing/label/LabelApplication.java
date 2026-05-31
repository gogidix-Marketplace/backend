package com.gogidix.shared.warehousing.label;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"com.gogidix.shared.warehousing.label"})
@EnableMongoAuditing
@OpenAPIDefinition(
    info = @Info(
        title = "Label Service API",
        version = "1.0.0",
        description = "Multi-tenant shipping label generation service",
        contact = @Contact(name = "Gogidix Team", email = "support@gogidix.com")
    )
)
public class LabelApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabelApplication.class, args);
    }
}
