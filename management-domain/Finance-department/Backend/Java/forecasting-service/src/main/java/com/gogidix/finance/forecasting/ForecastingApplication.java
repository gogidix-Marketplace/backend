package com.gogidix.finance.forecasting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ForecastingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForecastingApplication.class, args);
    }
}
