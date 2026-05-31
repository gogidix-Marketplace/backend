package com.gogidix.shared.infrastructure.services.infrastructure.caching.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.model.CacheEntry;
import com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.port.in.CachePort;
import com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.port.out.CacheRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Application service implementing CachePort.
 * Handles caching operations with multi-tenant support.
 */
@Service
public class CacheService implements CachePort {

    private static final Logger log = LoggerFactory.getLogger(CacheService.class);
    private static final long DEFAULT_TTL_SECONDS = 3600; // 1 hour

    private final CacheRepositoryPort cacheRepository;
    private final ObjectMapper objectMapper;
    private final TenantContextHolder tenantContextHolder;

    public CacheService(CacheRepositoryPort cacheRepository, ObjectMapper objectMapper,
                       TenantContextHolder tenantContextHolder) {
        this.cacheRepository = cacheRepository;
        this.objectMapper = objectMapper;
        this.tenantContextHolder = tenantContextHolder;
    }

    @Override
    public void put(String key, Object value, long ttl) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(ttl);

            CacheEntry entry = new CacheEntry(tenantId, key, serializedValue, ttl,
                    LocalDateTime.now(), expiresAt);
            entry.setAccessCount(0L);

            cacheRepository.save(entry);
            log.debug("Cached key: {} for tenant: {}", key, tenantId);
        } catch (Exception e) {
            log.error("Error caching key: {} for tenant: {}", key, tenantId, e);
            throw new RuntimeException("Failed to cache value", e);
        }
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, DEFAULT_TTL_SECONDS);
    }

    @Override
    public Object get(String key) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        Optional<CacheEntry> entryOpt = cacheRepository.findByTenantIdAndKey(tenantId, key);

        if (entryOpt.isEmpty()) {
            return null;
        }

        CacheEntry entry = entryOpt.get();

        if (entry.isExpired()) {
            cacheRepository.deleteByTenantIdAndKey(tenantId, key);
            return null;
        }

        entry.updateAccess();
        cacheRepository.save(entry);

        try {
            return objectMapper.readValue(entry.getSerializedValue(), Object.class);
        } catch (Exception e) {
            log.error("Error deserializing cached key: {} for tenant: {}", key, tenantId, e);
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        try {
            return objectMapper.convertValue(value, type);
        } catch (Exception e) {
            log.error("Error converting cached value to type: {}", type.getName(), e);
            return null;
        }
    }

    @Override
    public void evict(String key) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        cacheRepository.deleteByTenantIdAndKey(tenantId, key);
        log.debug("Evicted key: {} for tenant: {}", key, tenantId);
    }

    @Override
    public void clear() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        cacheRepository.deleteByTenantId(tenantId);
        log.info("Cleared all cache entries for tenant: {}", tenantId);
    }

    @Override
    public void putAll(Map<String, Object> entries, long ttl) {
        entries.forEach((key, value) -> put(key, value, ttl));
    }

    @Override
    public Map<String, Object> getAll(Set<String> keys) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        List<CacheEntry> entries = cacheRepository.findByTenantIdAndKeyIn(tenantId, keys);

        Map<String, Object> result = new HashMap<>();

        for (CacheEntry entry : entries) {
            if (entry.isExpired()) {
                cacheRepository.deleteByTenantIdAndKey(tenantId, entry.getKey());
                continue;
            }

            entry.updateAccess();
            cacheRepository.save(entry);

            try {
                Object value = objectMapper.readValue(entry.getSerializedValue(), Object.class);
                result.put(entry.getKey(), value);
            } catch (Exception e) {
                log.error("Error deserializing cached key: {}", entry.getKey(), e);
            }
        }

        return result;
    }

    @Override
    public void evictAll(Set<String> keys) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        cacheRepository.deleteByTenantIdAndKeyIn(tenantId, keys);
        log.debug("Evicted {} keys for tenant: {}", keys.size(), tenantId);
    }

    @Override
    public boolean exists(String key) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        Optional<CacheEntry> entry = cacheRepository.findByTenantIdAndKey(tenantId, key);

        if (entry.isEmpty()) {
            return false;
        }

        if (entry.get().isExpired()) {
            cacheRepository.deleteByTenantIdAndKey(tenantId, key);
            return false;
        }

        return true;
    }

    @Override
    public long getTtl(String key) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        Optional<CacheEntry> entry = cacheRepository.findByTenantIdAndKey(tenantId, key);

        if (entry.isEmpty() || entry.get().isExpired()) {
            return -1;
        }

        LocalDateTime expiresAt = entry.get().getExpiresAt();
        if (expiresAt == null) {
            return -1;
        }

        long remainingSeconds = java.time.Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
        return Math.max(0, remainingSeconds);
    }

    @Override
    public Map<String, Object> getStats() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        long totalCount = cacheRepository.countByTenantId(tenantId);
        List<CacheEntry> expiredEntries = cacheRepository.findExpiredEntries(tenantId, LocalDateTime.now());

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEntries", totalCount);
        stats.put("expiredEntries", expiredEntries.size());
        stats.put("activeEntries", totalCount - expiredEntries.size());
        stats.put("tenantId", tenantId);

        return stats;
    }
}
