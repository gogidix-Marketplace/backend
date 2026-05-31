package com.gogidix.aiservices.aiorchestrationservice.test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Test configuration for @WebMvcTest slice tests.
 * Placed in a higher package (".test") to be found before the main application.
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {
    MongoAutoConfiguration.class,
    org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class,
    SecurityAutoConfiguration.class
})
@ComponentScan(basePackages = "com.gogidix.aiservices.aiorchestrationservice.interfaces.rest")
public class TestConfig {
}
