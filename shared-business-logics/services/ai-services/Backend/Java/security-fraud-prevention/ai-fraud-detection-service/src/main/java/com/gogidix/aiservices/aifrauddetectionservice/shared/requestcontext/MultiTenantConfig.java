package com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for multi-tenancy support.
 * <p>
 * This configuration enables aspect-oriented programming for tenant context
 * propagation and registers necessary beans for tenant management.
 */
@Configuration
@EnableAspectJAutoProxy
public class MultiTenantConfig {

    // Note: TenantContextAspect is auto-detected via @Component annotation
    // Note: TenantContextRequestFilter is auto-detected via @Component annotation
}
