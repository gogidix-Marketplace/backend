package com.gogidix.shared.infrastructure.core.tenancy.config;

import com.gogidix.shared.infrastructure.core.tenancy.interceptor.TenantInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for multi-tenant support.
 * <p>
 * Registers the TenantInterceptor with Spring MVC to automatically
 * extract tenant ID from HTTP headers and set it in the tenant context.
 */
@Configuration
public class TenantConfig implements WebMvcConfigurer {

    private final TenantInterceptor tenantInterceptor;

    /**
     * Creates a new TenantConfig.
     *
     * @param tenantInterceptor the tenant interceptor to register
     */
    public TenantConfig(TenantInterceptor tenantInterceptor) {
        this.tenantInterceptor = tenantInterceptor;
    }

    /**
     * Registers the tenant interceptor for all API paths.
     *
     * @param registry the interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor)
                .addPathPatterns("/api/**")
                .order(1); // High priority - should run early
    }
}
