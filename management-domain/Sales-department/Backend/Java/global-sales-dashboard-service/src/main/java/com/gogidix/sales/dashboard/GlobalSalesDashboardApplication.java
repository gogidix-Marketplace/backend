package com.gogidix.sales.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Global Sales Dashboard Service Application
 * Multi-tenant SaaS hexagonal architecture for global sales metrics aggregation
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
@EnableScheduling
public class GlobalSalesDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalSalesDashboardApplication.class, args);
    }
}
