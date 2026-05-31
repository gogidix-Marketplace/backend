package com.gogidix.cargo.courier.gateway.handler;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"error\":\"Internal Server Error\",\"message\":\"" + ex.getMessage() + "\"}";
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}