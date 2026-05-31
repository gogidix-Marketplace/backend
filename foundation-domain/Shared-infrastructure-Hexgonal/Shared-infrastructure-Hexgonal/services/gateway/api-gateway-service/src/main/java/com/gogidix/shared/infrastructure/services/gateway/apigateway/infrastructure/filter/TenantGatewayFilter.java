package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * Tenant propagation gateway filter.
 * <p>
 * Extracts tenant ID from incoming request and forwards it to downstream services.
 */
@Slf4j
@Component
public class TenantGatewayFilter extends AbstractGatewayFilterFactory<TenantGatewayFilter.Config> {

    public TenantGatewayFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String tenantId = exchange.getRequest().getHeaders().getFirst("X-Tenant-ID");

            if (tenantId == null || tenantId.isEmpty()) {
                tenantId = exchange.getRequest().getHeaders().getFirst("X-Tenant");
            }

            if (tenantId != null && !tenantId.isEmpty()) {
                log.debug("Forwarding request with tenant ID: {}", tenantId);
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Configuration properties if needed
    }
}
