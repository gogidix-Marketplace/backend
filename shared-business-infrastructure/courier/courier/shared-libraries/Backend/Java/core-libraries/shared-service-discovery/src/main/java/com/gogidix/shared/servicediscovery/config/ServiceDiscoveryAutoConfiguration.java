package com.gogidix.shared.servicediscovery.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-Configuration for Service Discovery
 *
 * <p>Automatically configures service discovery and registration when
 * shared-service-discovery dependency is on the classpath.</p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Configuration
@EnableFeignClients(basePackages = "com.gogidix")
@EnableConfigurationProperties(ServiceDiscoveryProperties.class)
@ConditionalOnProperty(name = "gogidix.service-discovery.enabled", havingValue = "true", matchIfMissing = true)
public class ServiceDiscoveryAutoConfiguration {

    // Beans are automatically registered via @Component annotations:
    // - ServiceRegistrationClient
    // - ServiceDiscoveryClient
    // - TenantAwareFeignInterceptor
}
