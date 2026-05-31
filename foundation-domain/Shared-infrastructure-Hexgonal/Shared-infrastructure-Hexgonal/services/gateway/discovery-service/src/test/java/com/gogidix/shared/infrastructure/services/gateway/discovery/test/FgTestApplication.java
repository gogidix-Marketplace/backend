package com.gogidix.shared.infrastructure.services.gateway.discovery.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Minimal test configuration for discovery-service.
 * Excludes unnecessary auto-configurations for fast isolated testing.
 */
@Configuration
@EnableAutoConfiguration(exclude = {
    org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean.class
})
public class FgTestApplication {
}
