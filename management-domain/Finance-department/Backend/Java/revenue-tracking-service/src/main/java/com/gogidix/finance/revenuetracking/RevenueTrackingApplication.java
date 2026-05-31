package com.gogidix.finance.revenuetracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class RevenueTrackingApplication {
    public static void main(String[] args) {
        SpringApplication.run(RevenueTrackingApplication.class, args);
    }
}
