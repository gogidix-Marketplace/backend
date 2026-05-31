package com.gogidix.cargo.courier.gateway.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FallbackHandler {

    public Mono<ServerResponse> handle(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"status\":\"UP\",\"message\":\"Service unavailable, please try again\"}");
    }
}