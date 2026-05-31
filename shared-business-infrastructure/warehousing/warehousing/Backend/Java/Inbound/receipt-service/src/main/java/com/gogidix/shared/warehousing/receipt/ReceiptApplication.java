package com.gogidix.shared.warehousing.receipt;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@ComponentScan(basePackages = {"com.gogidix.shared.warehousing.receipt"})
@EnableMongoAuditing
@EnableKafka
@OpenAPIDefinition(
    info = @Info(
        title = "Receipt Service API",
        version = "1.0.0",
        description = "Multi-tenant goods receipt processing service",
        contact = @Contact(name = "Gogidix Team", email = "support@gogidix.com")
    )
)
public class ReceiptApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiptApplication.class, args);
    }
}
