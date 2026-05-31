package com.gogidix.finance.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Currency Service Application
 * Main Spring Boot application class
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.finance.currency")
@EnableMongoRepositories(basePackages = "com.gogidix.finance.currency.domain.repository")
public class CurrencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyApplication.class, args);
    }
}
