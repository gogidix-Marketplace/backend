package com.gogidix.aiservices.aigatewayservice.test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import com.gogidix.aiservices.aigatewayservice.interfaces.rest.GatewayRouteController;

/**
 * Minimal test configuration for @WebMvcTest slice tests.
 * Uses @Import to directly load only the controller, avoiding component scan.
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {
    MongoAutoConfiguration.class,
    org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class,
    SecurityAutoConfiguration.class
})
@Import(GatewayRouteController.class)
public class TestConfig {
}
