package com.gogidix.cargo.aiplatform.gateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class FallbackHandler {

    public Mono<ServerResponse> handleServiceUnavailable(ServerWebExchange exchange) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("error", "Service Unavailable");
        body.put("message", "The requested service is currently unavailable. Please try again later.");
        body.put("path", exchange.getRequest().getPath().value());

        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body);
    }

    public Mono<ServerResponse> handleCircuitBreakerFallback(ServerWebExchange exchange) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("error", "Circuit Breaker Open");
        body.put("message", "Service temporarily unavailable due to circuit breaker. Please retry after some time.");
        body.put("path", exchange.getRequest().getPath().value());

        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body);
    }
}