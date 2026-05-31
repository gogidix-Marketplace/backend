package com.gogidix.aiservices.aireporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AiReportingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiReportingServiceApplication.class, args);
    }
}
