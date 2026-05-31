package com.gogidix.pricingcompliance.globalpricingengineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GlobalPricingEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(GlobalPricingEngineApplication.class, args);
    }
}