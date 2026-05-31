package com.gogidix.platform.metering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Usage Metering Service Application
 *
 * Resource usage tracking and billing analytics service providing:
 * - Real-time usage collection (API calls, storage, bandwidth)
 * - Metric aggregation (hourly, daily, monthly)
 * - Quota management and enforcement
 * - Usage analytics and forecasting
 * - Billing data preparation
 *
 * @author Gogidix Platform Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class UsageMeteringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsageMeteringServiceApplication.class, args);
    }
}
