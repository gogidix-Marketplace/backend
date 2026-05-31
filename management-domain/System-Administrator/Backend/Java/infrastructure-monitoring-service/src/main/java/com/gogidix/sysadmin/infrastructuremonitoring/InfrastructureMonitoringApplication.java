package com.gogidix.sysadmin.infrastructuremonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gogidix.sysadmin.infrastructuremonitoring.domain.repository")
@EnableCaching
@EnableScheduling
public class InfrastructureMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrastructureMonitoringApplication.class, args);
    }
}
