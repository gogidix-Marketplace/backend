package com.gogidix.dashboard.aggregation.infrastructure.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign client configuration for service integration.
 */
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor tenantInterceptor() {
        return template -> {
            // Tenant ID will be added from the request context
            String tenantId = com.gogidix.dashboard.shared.dto.TenantContext.getCurrentTenantId();
            if (tenantId != null) {
                template.header("X-Tenant-ID", tenantId);
            }
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    private static class CustomErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, feign.Response response) {
            return new Default().decode(methodKey, response);
        }
    }
}
