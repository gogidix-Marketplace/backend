package com.gogidix.finance.globalfinancedashboard.infrastructure.config;

import com.gogidix.finance.globalfinancedashboard.infrastructure.security.TenantInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Configuration for Global Finance Dashboard Service
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TenantInterceptor tenantInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/actuator/**", "/swagger-ui/**", "/api-docs/**");
    }
}
