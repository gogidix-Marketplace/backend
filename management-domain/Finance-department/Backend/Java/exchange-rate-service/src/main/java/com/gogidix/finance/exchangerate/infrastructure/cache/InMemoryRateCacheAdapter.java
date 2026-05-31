package com.gogidix.finance.exchangerate.infrastructure.cache;

import com.gogidix.finance.exchangerate.domain.port.out.RateCachePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class InMemoryRateCacheAdapter implements RateCachePort {

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Override
    public void cacheRate(String currencyPair, BigDecimal rate, long ttlSeconds) {
        long expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        cache.put(currencyPair, new CacheEntry(rate, expiryTime));
        log.debug("Cached rate for {}: {}", currencyPair, rate);
    }

    @Override
    public Optional<BigDecimal> getCachedRate(String currencyPair) {
        CacheEntry entry = cache.get(currencyPair);

        if (entry == null) {
            return Optional.empty();
        }

        if (System.currentTimeMillis() > entry.expiryTime) {
            cache.remove(currencyPair);
            return Optional.empty();
        }

        return Optional.of(entry.rate);
    }

    @Override
    public void evict(String currencyPair) {
        cache.remove(currencyPair);
        log.debug("Evicted cache for {}", currencyPair);
    }

    @Override
    public void clear() {
        cache.clear();
        log.debug("Cleared all cache entries");
    }

    private static class CacheEntry {
        final BigDecimal rate;
        final long expiryTime;

        CacheEntry(BigDecimal rate, long expiryTime) {
            this.rate = rate;
            this.expiryTime = expiryTime;
        }
    }
}
