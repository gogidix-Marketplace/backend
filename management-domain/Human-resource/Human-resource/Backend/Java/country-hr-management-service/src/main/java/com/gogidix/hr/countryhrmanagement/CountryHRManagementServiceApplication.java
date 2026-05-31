package com.gogidix.hr.countryhrmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CountryHRManagementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CountryHRManagementServiceApplication.class, args);
    }
}
