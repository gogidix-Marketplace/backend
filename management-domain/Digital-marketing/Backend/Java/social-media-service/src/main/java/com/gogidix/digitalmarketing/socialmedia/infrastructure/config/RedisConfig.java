package com.gogidix.digitalmarketing.socialmedia.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

/**
 * Redis Cache Configuration for Social Media Service
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * Default cache TTL - 15 minutes
     */
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(15);

    /**
     * Short cache TTL - 5 minutes for frequently changing data
     */
    private static final Duration SHORT_TTL = Duration.ofMinutes(5);

    /**
     * Long cache TTL - 1 hour for stable data
     */
    private static final Duration LONG_TTL = Duration.ofHours(1);

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
            .entryTtl(DEFAULT_TTL)
            .disableCachingNullValues();

        RedisCacheConfiguration shortCacheConfig = defaultConfig.entryTtl(SHORT_TTL);
        RedisCacheConfiguration longCacheConfig = defaultConfig.entryTtl(LONG_TTL);

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withCacheConfiguration("accounts", longCacheConfig)
            .withCacheConfiguration("posts", shortCacheConfig)
            .withCacheConfiguration("engagement", shortCacheConfig)
            .withCacheConfiguration("content", longCacheConfig)
            .withCacheConfiguration("calendar", defaultConfig)
            .build();
    }

    @Bean
    public ObjectMapper redisObjectMapper() {
        return JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    }
}
