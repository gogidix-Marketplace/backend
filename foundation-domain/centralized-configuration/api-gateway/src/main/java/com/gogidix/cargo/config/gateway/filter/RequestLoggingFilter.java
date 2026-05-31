package com.gogidix.cargo.config.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String HEADER_CORRELATION_ID = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders().getFirst(HEADER_CORRELATION_ID);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        String finalCorrelationId = correlationId;
        exchange.getRequest().mutate().header(HEADER_CORRELATION_ID, finalCorrelationId).build();

        Instant start = Instant.now();
        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getPath().value();
        String tenantId = exchange.getRequest().getHeaders().getFirst("X-Tenant-ID");

        log.info("[{}] {} {} tenant={} start={}", finalCorrelationId, method, path, tenantId, start);

        return chain.filter(exchange).doFinally(signalType -> {
            long durationMs = Instant.now().toEpochMilli() - start.toEpochMilli();
            log.info("[{}] {} {} tenant={} status={} duration={}ms",
                    finalCorrelationId, method, path, tenantId,
                    exchange.getResponse().getStatusCode(), durationMs);
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}