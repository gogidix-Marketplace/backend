package com.gogidix.customersupport.countrysupportdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gogidix.customersupport.countrysupportdashboard")
public class CountrySupportDashboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CountrySupportDashboardServiceApplication.class);
        app.run(args);
    }
}
