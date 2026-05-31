package com.gogidix.globalbusinessmanagement.multicurrency.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache configuration for Multi-Currency Service.
 */
@Slf4j
@Configuration
@EnableCaching
public class MultiCurrencyServiceConfig {

    public static final String EXCHANGE_RATES_CACHE = "exchangeRates";
    public static final String CURRENCY_CACHE = "currency";
    public static final String CURRENCY_PAIR_CACHE = "currencyPair";
    public static final String ACCOUNT_CACHE = "account";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
            EXCHANGE_RATES_CACHE,
            CURRENCY_CACHE,
            CURRENCY_PAIR_CACHE,
            ACCOUNT_CACHE
        );

        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(2000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .recordStats());

        log.info("Configured Caffeine cache manager for Multi-Currency Service");

        return cacheManager;
    }
}
