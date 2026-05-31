package com.gogidix.shared.infrastructure.services.gateway.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {
        "com.gogidix.shared.infrastructure.services.gateway.apigateway",
        "com.gogidix.shared.infrastructure.core.tenancy",
        "com.gogidix.shared"
})
@EnableDiscoveryClient
public class ApiGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayServiceApplication.class, args);
    }
}
