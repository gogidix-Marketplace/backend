package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Audit Gateway Filter
 *
 * <p>Logs all cross-domain requests for audit trail and monitoring.</p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Slf4j
public class AuditGatewayFilter implements GatewayFilter, Ordered {

    private final ConcurrentHashMap<String, ApiMetrics> metrics = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String requestId = java.util.UUID.randomUUID().toString().substring(0, 12);
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();
        String tenantId = exchange.getRequest().getHeaders().getFirst("X-Tenant-ID");

        // Add request ID to response
        exchange.getResponse().getHeaders().add("X-Request-ID", requestId);

        log.info("[{}] {} {} | tenant: {}", requestId, method, path, tenantId);

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    long duration = System.currentTimeMillis() - startTime;
                    int statusCode = exchange.getResponse().getStatusCode() != null ?
                            exchange.getResponse().getStatusCode().value() : 0;

                    log.info("[{}] Response: {} {} | status: {} | duration: {}ms",
                            requestId, method, path, statusCode, duration);

                    updateMetrics(path, method, statusCode, duration);
                })
                .doOnError(throwable -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.error("[{}] Error: {} {} | error: {}", requestId, method, path,
                            throwable.getMessage(), throwable);
                });
    }

    @Override
    public int getOrder() {
        return 50;
    }

    private void updateMetrics(String path, String method, int statusCode, long duration) {
        String key = method + ":" + path;

        metrics.compute(key, (k, m) -> {
            if (m == null) {
                m = new ApiMetrics();
            }
            m.totalRequests.incrementAndGet();
            m.totalDuration.addAndGet(duration);
            if (statusCode >= 400) {
                m.errorCount.incrementAndGet();
            }
            if (duration > 1000) {
                m.slowCount.incrementAndGet();
            }
            return m;
        });
    }

    /**
     * Get metrics snapshot
     */
    public java.util.Map<String, MetricsSnapshot> getMetrics() {
        java.util.Map<String, MetricsSnapshot> snapshot = new java.util.HashMap<>();

        metrics.forEach((key, value) -> {
            long total = value.totalRequests.get();
            snapshot.put(key, new MetricsSnapshot(
                    total,
                    value.errorCount.get(),
                    value.slowCount.get(),
                    total > 0 ? value.totalDuration.get() / total : 0,
                    total > 0 ? (value.errorCount.get() * 100.0 / total) : 0
            ));
        });

        return snapshot;
    }

    private static class ApiMetrics {
        AtomicLong totalRequests = new AtomicLong(0);
        AtomicLong errorCount = new AtomicLong(0);
        AtomicLong slowCount = new AtomicLong(0);
        AtomicLong totalDuration = new AtomicLong(0);
    }

    public record MetricsSnapshot(
        long totalRequests,
        long errorCount,
        long slowCount,
        long averageDurationMs,
        double errorRate
    ) {}
}
