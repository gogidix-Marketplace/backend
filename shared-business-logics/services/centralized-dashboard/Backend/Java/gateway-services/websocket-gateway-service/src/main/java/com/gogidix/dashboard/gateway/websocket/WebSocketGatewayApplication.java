package com.gogidix.dashboard.gateway.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for WebSocket Gateway Service.
 *
 * <p>This service provides real-time WebSocket connections with Redis pub/sub support
 * for broadcasting updates to connected dashboard clients.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>WebSocket endpoint management</li>
 *   <li>Redis pub/sub for message broadcasting</li>
 *   <li>Topic-based subscriptions</li>
 *   <li>Tenant isolation</li>
 *   <li>Connection heartbeat and monitoring</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class WebSocketGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketGatewayApplication.class, args);
    }
}
