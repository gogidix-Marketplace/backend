package com.gogidix.aiservices.aipersonalizationservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ai.inference")
public class AiServiceProperties {
    private String baseUrl;
    private String recommendationEndpoint;
    private String affinityEndpoint;
    private String segmentEndpoint;
    private int timeout = 5000;
    private int maxRetries = 3;
}
