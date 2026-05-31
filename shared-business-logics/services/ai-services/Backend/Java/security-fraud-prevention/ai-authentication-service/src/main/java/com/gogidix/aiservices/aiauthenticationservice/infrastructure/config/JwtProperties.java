package com.gogidix.aiservices.aiauthenticationservice.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret = "default-secret-key-for-development-only-must-be-at-least-32-chars";
    private int accessTokenExpiration = 3600;
    private int refreshTokenExpiration = 2592000;
    private String issuer = "ai-authentication-service";
}
