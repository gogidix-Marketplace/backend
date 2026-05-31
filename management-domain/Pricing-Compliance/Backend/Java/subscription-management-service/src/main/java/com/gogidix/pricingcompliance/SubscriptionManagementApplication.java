package com.gogidix.pricingcompliance.subscriptionmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SubscriptionManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SubscriptionManagementApplication.class, args);
    }
}