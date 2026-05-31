package com.gogidix.centralconfiguration.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Config Server.
 *
 * <p>Spring Cloud Config Server for centralized configuration management
 * with multi-tenant SaaS architecture.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Hexagonal architecture with DDD</li>
 *   <li>Multi-tenancy with tenant isolation</li>
 *   <li>CQRS pattern for command/query separation</li>
 *   <li>Event-driven architecture with Kafka</li>
 *   <li>Version-controlled configuration storage</li>
 * </ul>
 */
@SpringBootApplication
@EnableConfigServer
@EnableJpaAuditing
@EnableScheduling
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
