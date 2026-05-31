package com.gogidix.aiservices.aidocumentprocessingservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ocr.service")
public class OcrServiceProperties {
    private String serviceUrl = "http://localhost:8080";
    private String healthEndpoint = "/health";
    private int timeoutMs = 30000;
}
