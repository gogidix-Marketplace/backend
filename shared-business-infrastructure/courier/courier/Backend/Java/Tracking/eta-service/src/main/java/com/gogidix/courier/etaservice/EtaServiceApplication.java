package com.gogidix.courier.etaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ETA Service Application.
 *
 * Estimated Time of Arrival Service for GOGIDIX Courier Services.
 * Provides traffic-aware ETA calculations with real-time updates.
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.courier.etaservice")
@EnableCaching
@EnableKafka
@EnableAsync
@EnableScheduling
public class EtaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtaServiceApplication.class, args);
    }
}
