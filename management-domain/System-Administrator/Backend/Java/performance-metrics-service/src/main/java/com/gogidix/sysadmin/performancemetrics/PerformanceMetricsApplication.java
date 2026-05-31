package com.gogidix.sysadmin.performancemetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.sysadmin.performancemetrics.domain.repository")
@EnableCaching
@EnableScheduling
public class PerformanceMetricsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceMetricsApplication.class, args);
    }
}
