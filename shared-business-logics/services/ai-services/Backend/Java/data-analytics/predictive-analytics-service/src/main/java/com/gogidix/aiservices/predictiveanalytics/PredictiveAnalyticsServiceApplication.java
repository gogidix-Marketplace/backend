package com.gogidix.aiservices.predictiveanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PredictiveAnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PredictiveAnalyticsServiceApplication.class, args);
    }
}
