package com.gogidix.ecommerce.paymentmethod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.ecommerce.paymentmethod.domain.repository")
public class PaymentMethodApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMethodApplication.class, args);
    }
}
