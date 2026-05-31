package com.gogidix.shared.utilities.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache configuration for Shared Utilities service
 * Configures Redis-based caching with different TTL for different cache types
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        
        // Default cache configuration
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // Cache-specific configurations with different TTL
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Utility cache - 1 hour TTL
        cacheConfigurations.put("utility-cache", defaultConfig.entryTtl(Duration.ofHours(1)));
        
        // DateTime cache - 30 minutes TTL (results change less frequently)
        cacheConfigurations.put("datetime-cache", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // JSON cache - 30 minutes TTL
        cacheConfigurations.put("json-cache", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // File processing cache - 15 minutes TTL (files can change more frequently)
        cacheConfigurations.put("file-cache", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        // Validation cache - 15 minutes TTL
        cacheConfigurations.put("validation-cache", defaultConfig.entryTtl(Duration.ofMinutes(15)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }
}