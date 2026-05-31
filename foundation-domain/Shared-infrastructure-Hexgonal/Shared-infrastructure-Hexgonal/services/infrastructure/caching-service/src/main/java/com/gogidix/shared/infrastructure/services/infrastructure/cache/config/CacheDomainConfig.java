package com.gogidix.shared.infrastructure.services.infrastructure.cache.config;

import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.aggregate.CacheRegistry;
import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.event.CacheEntryCreatedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.event.CacheEntryEvictedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.event.CacheInvalidatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for cache domain layer.
 * Creates aggregate roots and wires up event handlers.
 */
@Configuration
public class CacheDomainConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheDomainConfig.class);

    @Bean
    public CacheRegistry cacheRegistry() {
        CacheRegistry registry = new CacheRegistry();

        // Register event handlers
        registry.onCacheEntryCreated(this::handleCacheEntryCreated);
        registry.onCacheEntryEvicted(this::handleCacheEntryEvicted);
        registry.onCacheInvalidated(this::handleCacheInvalidated);

        log.info("CacheRegistry aggregate initialized with event handlers");

        return registry;
    }

    private void handleCacheEntryCreated(CacheEntryCreatedEvent event) {
        log.debug("Cache entry created: key={}, region={}, ttl={}s",
            event.getKey(), event.getRegion(), event.getTtlSeconds());
    }

    private void handleCacheEntryEvicted(CacheEntryEvictedEvent event) {
        log.debug("Cache entry evicted: key={}, region={}, reason={}",
            event.getKey(), event.getRegion(), event.getReason());
    }

    private void handleCacheInvalidated(CacheInvalidatedEvent event) {
        log.info("Cache invalidated: scope={}, type={}, entries={}",
            event.getScope(), event.getScopeType(), event.getEntriesAffected());
    }
}
