package com.gogidix.hr.benefitsadministration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class BenefitsAdministrationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BenefitsAdministrationServiceApplication.class, args);
    }
}
