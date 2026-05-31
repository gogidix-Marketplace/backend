package com.gogidix.customersupport.knowledgebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.knowledgebase")
@EnableMongoAuditing
public class KnowledgeBaseServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(KnowledgeBaseServiceApplication.class);
        app.run(args);
    }
}
