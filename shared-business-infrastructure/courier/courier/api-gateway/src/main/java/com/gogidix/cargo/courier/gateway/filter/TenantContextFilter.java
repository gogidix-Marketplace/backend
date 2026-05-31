package com.gogidix.cargo.courier.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TenantContextFilter implements GlobalFilter, Ordered {

    private static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String tenantId = exchange.getRequest().getHeaders().getFirst(TENANT_HEADER);
        if (tenantId != null) {
            exchange.getRequest().mutate().header(TENANT_HEADER, tenantId).build();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }
}