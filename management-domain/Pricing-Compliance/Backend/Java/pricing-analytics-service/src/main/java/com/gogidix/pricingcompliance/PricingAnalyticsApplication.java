package com.gogidix.pricingcompliance.pricinganalyticsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PricingAnalyticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PricingAnalyticsApplication.class, args);
    }
}