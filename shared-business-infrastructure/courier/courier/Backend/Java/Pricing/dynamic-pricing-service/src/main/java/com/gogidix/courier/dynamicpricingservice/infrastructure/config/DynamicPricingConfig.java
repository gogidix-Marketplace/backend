package com.gogidix.courier.dynamicpricingservice.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class for Dynamic Pricing Service.
 */
@Configuration
@EnableKafka
@EnableScheduling
public class DynamicPricingConfig {

    // Bean configurations for dynamic pricing will be added here
    // Includes: Kafka producers, schedulers, cache configurations, etc.
}
