package com.gogidix.finance.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ExpenseTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackingApplication.class, args);
    }
}
