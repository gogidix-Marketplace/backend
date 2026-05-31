package com.gogidix.shared.ai.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cargonexus.ai")
public class AiClientConfig {

    private String openaiApiKey;
    private String anthropicApiKey;
    private String defaultModel = "gpt-4";
    private double defaultTemperature = 0.7;
    private int defaultMaxTokens = 4096;
    private int embeddingDimensions = 1536;
    private int requestTimeoutSeconds = 60;
}
