package com.gogidix.shared.warehousing.publicapi.availability.infrastructure.config;

import com.gogidix.shared.warehousing.publicapi.availability.infrastructure.security.TenantContext;
import com.gogidix.shared.warehousing.publicapi.availability.infrastructure.security.TenantInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration for Public Availability Service
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TenantContext tenantContext;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantInterceptor(tenantContext))
                .addPathPatterns("/**")
                .excludePathPatterns("/actuator/**", "/health", "/error");
    }
}
