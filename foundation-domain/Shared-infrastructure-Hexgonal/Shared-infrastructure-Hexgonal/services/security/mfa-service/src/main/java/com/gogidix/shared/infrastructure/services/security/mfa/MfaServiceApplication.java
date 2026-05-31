package com.gogidix.shared.infrastructure.services.security.mfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MfaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MfaServiceApplication.class, args);
    }
}
