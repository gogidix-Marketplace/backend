package com.gogidix.hr.documentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
@OpenAPIDefinition(
    info = @Info(
        title = "Document Management Service API",
        version = "1.0.0",
        description = "HR Document Management Service - Hexagonal Architecture Implementation",
        contact = @Contact(
            name = "Gogidix HR Team",
            email = "hr@gogidix.com"
        ),
        license = @License(
            name = "Proprietary",
            url = "https://gogidix.com"
        )
    )
)
public class DocumentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentManagementApplication.class, args);
    }
}
