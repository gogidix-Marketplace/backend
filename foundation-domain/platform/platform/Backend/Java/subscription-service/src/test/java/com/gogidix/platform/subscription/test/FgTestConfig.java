package com.gogidix.platform.subscription.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Test configuration for Financial-Grade tests.
 * Disables JPA auditing which requires entity scanning.
 */
@Configuration
public class FgTestConfig {
    // Disable JPA auditing for tests
}
