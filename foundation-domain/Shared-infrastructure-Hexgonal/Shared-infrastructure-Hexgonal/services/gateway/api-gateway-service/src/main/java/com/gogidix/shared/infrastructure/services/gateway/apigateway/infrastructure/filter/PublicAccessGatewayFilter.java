package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Public Access Gateway Filter
 *
 * <p>Allows public access without tenant authentication for public marketplace
 * and related services.</p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Slf4j
public class PublicAccessGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("Public access granted to: {}", exchange.getRequest().getPath());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -80;
    }
}
