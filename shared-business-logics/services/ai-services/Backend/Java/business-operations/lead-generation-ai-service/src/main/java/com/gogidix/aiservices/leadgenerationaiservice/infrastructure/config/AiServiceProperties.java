package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ai.service")
public class AiServiceProperties {
    private String baseUrl = "http://localhost:8080";
    private String scoringEndpoint = "/api/score";
    private String enrichmentEndpoint = "/api/enrich";
    private int timeout = 5000;
}
