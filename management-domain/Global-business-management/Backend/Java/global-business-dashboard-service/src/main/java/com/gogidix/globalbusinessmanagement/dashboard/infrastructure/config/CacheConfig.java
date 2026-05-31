package com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache configuration for Global Business Dashboard Service.
 * Configures Caffeine cache for high-performance caching.
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String GLOBAL_METRICS_CACHE = "globalMetrics";
    public static final String REGIONAL_SUMMARY_CACHE = "regionalSummary";
    public static final String COUNTRY_METRICS_CACHE = "countryMetrics";
    public static final String KPI_BOARD_CACHE = "kpiBoard";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
            GLOBAL_METRICS_CACHE,
            REGIONAL_SUMMARY_CACHE,
            COUNTRY_METRICS_CACHE,
            KPI_BOARD_CACHE
        );

        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats());

        log.info("Configured Caffeine cache manager with cache names: {}",
            String.join(", ", GLOBAL_METRICS_CACHE, REGIONAL_SUMMARY_CACHE, COUNTRY_METRICS_CACHE, KPI_BOARD_CACHE));

        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .recordStats();
    }
}
