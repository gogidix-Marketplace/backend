package com.gogidix.shared.servicediscovery.config;

import com.gogidix.shared.servicediscovery.client.ServiceDiscoveryClient;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Feign Request Interceptor for Tenant-Aware Service Calls
 *
 * <p>Automatically adds tenant context headers to all Feign client requests
 * for proper tenant isolation when calling other domain services.</p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Slf4j
@Component
public class TenantAwareFeignInterceptor implements RequestInterceptor {

    private static final String TENANT_ID_HEADER = "X-Tenant-ID";
    private static final String DOMAIN_HEADER = "X-Domain";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public void apply(RequestTemplate template) {
        // Add tenant ID from context
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null && !template.headers().containsKey(TENANT_ID_HEADER)) {
            template.header(TENANT_ID_HEADER, tenantId);
        }

        // Add domain from context
        String domain = TenantContext.getDomain();
        if (domain != null && !template.headers().containsKey(DOMAIN_HEADER)) {
            template.header(DOMAIN_HEADER, domain);
        }

        // Add correlation ID for tracing
        String correlationId = TenantContext.getCorrelationId();
        if (correlationId == null) {
            correlationId = generateCorrelationId();
        }
        if (!template.headers().containsKey(CORRELATION_ID_HEADER)) {
            template.header(CORRELATION_ID_HEADER, correlationId);
        }

        log.trace("Added headers to Feign request: tenant={}, domain={}, correlation={}",
                tenantId, domain, correlationId);
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
