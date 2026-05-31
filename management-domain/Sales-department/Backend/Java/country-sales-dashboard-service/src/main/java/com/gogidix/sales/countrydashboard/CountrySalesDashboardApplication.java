package com.gogidix.sales.countrydashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Country Sales Dashboard Service Application
 * Multi-tenant SaaS hexagonal architecture for country-specific sales metrics
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
@EnableCaching
@EnableScheduling
public class CountrySalesDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CountrySalesDashboardApplication.class, args);
    }
}
