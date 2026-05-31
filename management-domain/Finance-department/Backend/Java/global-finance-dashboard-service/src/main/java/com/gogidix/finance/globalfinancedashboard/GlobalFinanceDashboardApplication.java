package com.gogidix.finance.globalfinancedashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class GlobalFinanceDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(GlobalFinanceDashboardApplication.class, args);
    }
}
