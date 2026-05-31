package com.gogidix.hr.employeeselfservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableMongoAuditing
@EnableKafka
public class EmployeeSelfServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeSelfServiceApplication.class, args);
    }
}
