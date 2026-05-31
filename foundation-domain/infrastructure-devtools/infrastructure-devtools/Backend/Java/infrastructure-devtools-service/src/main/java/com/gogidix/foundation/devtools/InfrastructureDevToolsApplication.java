package com.gogidix.foundation.devtools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Infrastructure DevTools Service.
 *
 * <p>This service provides comprehensive development tools including:
 * <ul>
 *   <li>Developer portal and dashboard</li>
 *   <li>API testing tools</li>
 *   <li>Database query tools</li>
 *   <li>Logging and debugging utilities</li>
 *   <li>Deployment tools</li>
 *   <li>Documentation generation</li>
 * </ul>
 *
 * @author Gogidix Platform Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class InfrastructureDevToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrastructureDevToolsApplication.class, args);
    }
}
