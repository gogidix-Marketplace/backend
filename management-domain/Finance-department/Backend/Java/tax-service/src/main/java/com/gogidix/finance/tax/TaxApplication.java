package com.gogidix.finance.tax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class TaxApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxApplication.class, args);
    }
}
