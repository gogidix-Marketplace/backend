package com.gogidix.pricingcompliance.pricingcommandcenterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PricingCommandCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(PricingCommandCenterApplication.class, args);
    }
}