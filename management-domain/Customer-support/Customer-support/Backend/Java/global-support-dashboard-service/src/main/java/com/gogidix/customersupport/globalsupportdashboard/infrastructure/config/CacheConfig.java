package com.gogidix.customersupport.globalsupportdashboard.infrastructure.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache Configuration for Global Support Dashboard Service
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configure support metrics cache (5 minute TTL)
     */
    @Bean
    public ConcurrentMapCache supportMetricsCache() {
        return new ConcurrentMapCache("supportMetrics");
    }

    /**
     * Configure dashboard summary cache (2 minute TTL)
     */
    @Bean
    public ConcurrentMapCache dashboardSummaryCache() {
        return new ConcurrentMapCache("dashboardSummary");
    }

    /**
     * Configure regional metrics cache (5 minute TTL)
     */
    @Bean
    public ConcurrentMapCache regionalMetricsCache() {
        return new ConcurrentMapCache("regionalMetrics");
    }

    /**
     * Configure agent performance cache (5 minute TTL)
     */
    @Bean
    public ConcurrentMapCache agentPerformanceCache() {
        return new ConcurrentMapCache("agentPerformance");
    }
}
