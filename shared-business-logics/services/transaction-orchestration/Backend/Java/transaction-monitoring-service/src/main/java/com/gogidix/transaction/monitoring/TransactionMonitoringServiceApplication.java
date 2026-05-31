package com.gogidix.transaction.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableAsync
@EnableScheduling
public class TransactionMonitoringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionMonitoringServiceApplication.class, args);
    }
}
