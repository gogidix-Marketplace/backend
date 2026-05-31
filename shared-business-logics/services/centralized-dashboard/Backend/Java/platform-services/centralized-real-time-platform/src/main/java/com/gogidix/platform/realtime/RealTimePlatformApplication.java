package com.gogidix.platform.realtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main application class for Centralized Real-Time Platform Service.
 *
 * <p>This service implements real-time data streaming and WebSocket connectivity
 * with multi-tenant SaaS architecture using hexagonal architecture.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD</li>
 *   <li>WebSocket support for real-time communication</li>
 *   <li>Redis pub/sub for real-time data distribution</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Real-time analytics and dashboards</li>
 * </ul>
 */
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class RealTimePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealTimePlatformApplication.class, args);
    }
}
