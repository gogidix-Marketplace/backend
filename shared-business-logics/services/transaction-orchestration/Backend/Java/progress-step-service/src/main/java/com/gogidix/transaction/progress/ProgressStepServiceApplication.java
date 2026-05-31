package com.gogidix.transaction.progress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableAsync
public class ProgressStepServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgressStepServiceApplication.class, args);
    }
}
