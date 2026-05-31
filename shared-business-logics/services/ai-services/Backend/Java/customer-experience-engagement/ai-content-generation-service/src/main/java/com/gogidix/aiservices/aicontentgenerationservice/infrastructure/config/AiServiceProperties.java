package com.gogidix.aiservices.aicontentgenerationservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai.service")
public class AiServiceProperties {

    private String baseUrl = "http://localhost:8080";
    private String generationEndpoint = "/api/v1/generate";
    private String variationsEndpoint = "/api/v1/variations";
    private String optimizationEndpoint = "/api/v1/optimize";
    private int timeout = 30000;
    private int maxRetries = 3;
    private boolean enabled = true;
}
