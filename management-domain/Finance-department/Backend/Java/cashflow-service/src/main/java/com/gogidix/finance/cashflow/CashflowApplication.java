package com.gogidix.finance.cashflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Cashflow Service Application
 * Multi-tenant SaaS hexagonal architecture for Finance Department
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class CashflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashflowApplication.class, args);
    }
}
