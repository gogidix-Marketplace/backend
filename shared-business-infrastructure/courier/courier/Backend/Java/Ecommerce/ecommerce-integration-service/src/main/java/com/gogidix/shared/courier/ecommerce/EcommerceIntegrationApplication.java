package com.gogidix.shared.courier.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableMongoAuditing
@EnableKafka
@EnableAsync
public class EcommerceIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceIntegrationApplication.class, args);
    }
}
