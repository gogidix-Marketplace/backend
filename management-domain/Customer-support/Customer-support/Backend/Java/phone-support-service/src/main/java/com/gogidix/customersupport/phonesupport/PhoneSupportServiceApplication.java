package com.gogidix.customersupport.phonesupport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.phonesupport")
@EnableMongoAuditing
public class PhoneSupportServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PhoneSupportServiceApplication.class);
        app.run(args);
    }
}
