package com.gogidix.shared.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories
@EnableKafka
@EnableCaching
@EnableAsync
@EntityScan(basePackages = "com.gogidix.shared.audit.domain")
public class SharedAuditApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedAuditApplication.class, args);
    }
}