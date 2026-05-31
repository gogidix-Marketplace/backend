package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.config;

import com.gogidix.shared.multitenancy.interceptor.TenantInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TenantInterceptor tenantInterceptor;

    public WebMvcConfig(TenantInterceptor tenantInterceptor) {
        this.tenantInterceptor = tenantInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (tenantInterceptor == null) {
            return;
        }
        InterceptorRegistration registration = registry.addInterceptor(tenantInterceptor);
        if (registration != null) {
            registration.addPathPatterns("/api/**")
                    .excludePathPatterns("/api/auth/login", "/api/auth/register", "/api/auth/refresh")
                    .order(1);
        }
    }
}
