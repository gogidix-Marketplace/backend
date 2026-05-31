package com.gogidix.cargo.aiplatform.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TenantContextFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(TenantContextFilter.class);
    private static final String HEADER_TENANT_ID = "X-Tenant-ID";
    private static final String DEFAULT_TENANT = "default";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String tenantId = exchange.getRequest().getHeaders().getFirst(HEADER_TENANT_ID);
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = DEFAULT_TENANT;
        }

        String finalTenantId = tenantId;
        exchange.getRequest().mutate().header(HEADER_TENANT_ID, finalTenantId).build();

        return chain.filter(exchange).doFinally(signalType -> {
            log.debug("Request completed for tenant: {} path: {} status: {}",
                    finalTenantId,
                    exchange.getRequest().getPath().value(),
                    exchange.getResponse().getStatusCode());
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }
}