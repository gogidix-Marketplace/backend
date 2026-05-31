package com.gogidix.infrastructure.lockservice.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.infrastructure.lockservice.domain.model.*;
import com.gogidix.infrastructure.lockservice.domain.repository.LockRepository;
import io.lettuce.core.ScriptOutputType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis-based implementation of the LockRepository.
 * Uses Lua scripts for atomic operations to ensure distributed lock consistency.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisLockRepository implements LockRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String LOCK_PREFIX = "lock:";
    private static final String LOCK_HOLDER_PREFIX = "holder:";
    private static final String LOCK_INDEX_PREFIX = "index:";
    private static final String STATS_PREFIX = "stats:";

    // Lua script for atomic lock acquisition
    private static final String ACQUIRE_LOCK_SCRIPT =
            "local key = KEYS[1]\n" +
            "local lockData = ARGV[1]\n" +
            "local ttl = tonumber(ARGV[2])\n" +
            "local current = redis.call('GET', key)\n" +
            "if current == false then\n" +
            "  redis.call('SET', key, lockData)\n" +
            "  redis.call('EXPIRE', key, ttl)\n" +
            "  return 1\n" +
            "else\n" +
            "  return 0\n" +
            "end";

    // Lua script for atomic lock release with holder verification
    private static final String RELEASE_LOCK_SCRIPT =
            "local key = KEYS[1]\n" +
            "local holderId = ARGV[1]\n" +
            "local current = redis.call('GET', key)\n" +
            "if current == false then\n" +
            "  return -1\n" +
            "end\n" +
            "local data = cjson.decode(current)\n" +
            "if data.holderId == holderId then\n" +
            "  redis.call('DEL', key)\n" +
            "  return 1\n" +
            "else\n" +
            "  return 0\n" +
            "end";

    // Lua script for extending lock TTL
    private static final String EXTEND_LOCK_SCRIPT =
            "local key = KEYS[1]\n" +
            "local holderId = ARGV[1]\n" +
            "local additionalTtl = tonumber(ARGV[2])\n" +
            "local current = redis.call('GET', key)\n" +
            "if current == false then\n" +
            "  return -1\n" +
            "end\n" +
            "local data = cjson.decode(current)\n" +
            "if data.holderId == holderId then\n" +
            "  redis.call('EXPIRE', key, additionalTtl)\n" +
            "  return 1\n" +
            "else\n" +
            "  return 0\n" +
            "end";

    @Override
    public LockAcquisitionResult acquireLock(LockRequest request) {
        LocalDateTime startTime = LocalDateTime.now();
        String redisKey = buildRedisKey(request.getTenantId(), request.getResourceKey());

        try {
            // Build lock data
            Lock lock = buildLockFromRequest(request);
            String lockJson = objectMapper.writeValueAsString(lock);
            long ttlSeconds = request.getTtlSeconds() != null ? request.getTtlSeconds() : 300;

            // Try to acquire using Lua script for atomicity
            Long result = redisTemplate.execute(
                (RedisCallback<Long>) connection -> {
                    return connection.eval(
                        ACQUIRE_LOCK_SCRIPT.getBytes(),
                        ReturnType.INTEGER,
                        1,
                        redisKey.getBytes(StandardCharsets.UTF_8),
                        lockJson.getBytes(StandardCharsets.UTF_8),
                        String.valueOf(ttlSeconds).getBytes(StandardCharsets.UTF_8)
                    );
                }
            );

            if (result != null && result == 1) {
                lock.setStatus(LockStatus.LOCKED);
                lock.setAcquiredAt(LocalDateTime.now());
                lock.setExpiresAt(calculateExpiration(lock.getTtlSeconds()));

                // Add to holder index
                addToHolderIndex(lock);

                // Update statistics
                updateLockStatistics(lock.getTenantId(), true);

                return LockAcquisitionResult.success(lock, 0);
            } else {
                return LockAcquisitionResult.failure("Resource already locked");
            }
        } catch (Exception e) {
            log.error("Error acquiring lock for resource: {}", request.getResourceKey(), e);
            return LockAcquisitionResult.failure("Error acquiring lock: " + e.getMessage());
        }
    }

    @Override
    public LockReleaseResult releaseLock(String tenantId, String resourceKey, String holderId, String lockId) {
        String redisKey = buildRedisKey(tenantId, resourceKey);

        try {
            Long result = redisTemplate.execute(
                (RedisCallback<Long>) connection -> {
                    return connection.eval(
                        RELEASE_LOCK_SCRIPT.getBytes(),
                        ReturnType.INTEGER,
                        1,
                        redisKey.getBytes(StandardCharsets.UTF_8),
                        holderId.getBytes(StandardCharsets.UTF_8)
                    );
                }
            );

            if (result == null || result == -1) {
                return LockReleaseResult.failure("Lock not found or already expired");
            } else if (result == 0) {
                return LockReleaseResult.failure("Lock is held by a different holder");
            } else {
                // Remove from holder index
                removeFromHolderIndex(tenantId, holderId, lockId);

                return LockReleaseResult.success(
                    Lock.builder(lockId, tenantId, resourceKey)
                        .holderId(holderId)
                        .status(LockStatus.RELEASED)
                        .build()
                );
            }
        } catch (Exception e) {
            log.error("Error releasing lock for resource: {}", resourceKey, e);
            return LockReleaseResult.failure("Error releasing lock: " + e.getMessage());
        }
    }

    @Override
    public boolean extendLock(String tenantId, String resourceKey, String holderId, String lockId, long additionalTtlSeconds) {
        String redisKey = buildRedisKey(tenantId, resourceKey);

        try {
            Long result = redisTemplate.execute(
                (RedisCallback<Long>) connection -> {
                    return connection.eval(
                        EXTEND_LOCK_SCRIPT.getBytes(),
                        ReturnType.INTEGER,
                        1,
                        redisKey.getBytes(StandardCharsets.UTF_8),
                        holderId.getBytes(StandardCharsets.UTF_8),
                        String.valueOf(additionalTtlSeconds).getBytes(StandardCharsets.UTF_8)
                    );
                }
            );

            return result != null && result == 1;
        } catch (Exception e) {
            log.error("Error extending lock for resource: {}", resourceKey, e);
            return false;
        }
    }

    @Override
    public Optional<Lock> findLock(String tenantId, String resourceKey) {
        String redisKey = buildRedisKey(tenantId, resourceKey);
        String lockJson = redisTemplate.opsForValue().get(redisKey);

        if (lockJson != null) {
            try {
                Lock lock = objectMapper.readValue(lockJson, Lock.class);
                return Optional.of(lock);
            } catch (Exception e) {
                log.error("Error deserializing lock for resource: {}", resourceKey, e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Lock> findLockById(String lockId) {
        // Search across all tenants using scan
        Set<String> keys = redisTemplate.keys(LOCK_PREFIX + "*");
        if (keys != null) {
            for (String key : keys) {
                String lockJson = redisTemplate.opsForValue().get(key);
                if (lockJson != null) {
                    try {
                        Lock lock = objectMapper.readValue(lockJson, Lock.class);
                        if (lock.getLockId().toString().equals(lockId)) {
                            return Optional.of(lock);
                        }
                    } catch (Exception e) {
                        log.error("Error deserializing lock from key: {}", key, e);
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Lock> findActiveLocksByTenant(String tenantId) {
        String pattern = LOCK_PREFIX + tenantId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }

        return keys.stream()
            .map(key -> redisTemplate.opsForValue().get(key))
            .filter(Objects::nonNull)
            .map(json -> {
                try {
                    return objectMapper.readValue(json, Lock.class);
                } catch (Exception e) {
                    log.error("Error deserializing lock", e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .filter(lock -> lock.getStatus() == LockStatus.LOCKED && !lock.isExpired())
            .collect(Collectors.toList());
    }

    @Override
    public List<Lock> findLocksByHolder(String tenantId, String holderId) {
        String holderIndexKey = buildHolderIndexKey(tenantId, holderId);
        Set<String> lockIds = redisTemplate.opsForSet().members(holderIndexKey);

        if (lockIds == null || lockIds.isEmpty()) {
            return Collections.emptyList();
        }

        return lockIds.stream()
            .map(this::findLockById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(lock -> lock.getStatus() == LockStatus.LOCKED)
            .collect(Collectors.toList());
    }

    @Override
    public List<Lock> findExpiredLocks(String tenantId) {
        String pattern = tenantId != null
            ? LOCK_PREFIX + tenantId + ":*"
            : LOCK_PREFIX + "*";

        Set<String> keys = redisTemplate.keys(pattern);

        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDateTime now = LocalDateTime.now();

        return keys.stream()
            .map(key -> redisTemplate.opsForValue().get(key))
            .filter(Objects::nonNull)
            .map(json -> {
                try {
                    return objectMapper.readValue(json, Lock.class);
                } catch (Exception e) {
                    log.error("Error deserializing lock", e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .filter(lock -> lock.getExpiresAt() != null && lock.getExpiresAt().isBefore(now))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isLocked(String tenantId, String resourceKey) {
        String redisKey = buildRedisKey(tenantId, resourceKey);
        Boolean exists = redisTemplate.hasKey(redisKey);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public boolean isLockedBy(String tenantId, String resourceKey, String holderId) {
        Optional<Lock> lock = findLock(tenantId, resourceKey);
        return lock.map(l -> l.isHeldBy(holderId)).orElse(false);
    }

    @Override
    public boolean forceUnlock(String tenantId, String resourceKey) {
        String redisKey = buildRedisKey(tenantId, resourceKey);
        return Boolean.TRUE.equals(redisTemplate.delete(redisKey));
    }

    @Override
    public long cleanupExpiredLocks(String tenantId) {
        List<Lock> expiredLocks = findExpiredLocks(tenantId);

        long cleanedUp = expiredLocks.stream()
            .filter(lock -> {
                String redisKey = buildRedisKey(lock.getTenantId(), lock.getResourceKey());
                redisTemplate.delete(redisKey);
                removeFromHolderIndex(lock.getTenantId(), lock.getHolderId(), lock.getLockId().toString());
                return true;
            })
            .count();

        log.info("Cleaned up {} expired locks for tenant: {}", cleanedUp, tenantId);
        return cleanedUp;
    }

    @Override
    public long getActiveLockCount(String tenantId) {
        return findActiveLocksByTenant(tenantId).size();
    }

    // Helper methods

    private String buildRedisKey(String tenantId, String resourceKey) {
        return LOCK_PREFIX + tenantId + ":" + resourceKey;
    }

    private String buildHolderIndexKey(String tenantId, String holderId) {
        return LOCK_HOLDER_PREFIX + tenantId + ":" + holderId;
    }

    private Lock buildLockFromRequest(LockRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Long ttlSeconds = request.getTtlSeconds() != null ? request.getTtlSeconds() : 300L;

        return Lock.builder(
            request.getTenantId(),
            request.getResourceKey(),
            request.getHolderId()
        )
            .lockId(UUID.randomUUID())
            .lockType(request.getLockType())
            .status(LockStatus.AVAILABLE)
            .holderName(request.getHolderName())
            .ttlSeconds(ttlSeconds)
            .expiresAt(calculateExpiration(ttlSeconds))
            .metadata(request.getMetadata())
            .acquiredAt(now)
            .retryCount(0)
            .build();
    }

    private LocalDateTime calculateExpiration(Long ttlSeconds) {
        if (ttlSeconds == null || ttlSeconds <= 0) {
            return null;
        }
        return LocalDateTime.now().plusSeconds(ttlSeconds);
    }

    private void addToHolderIndex(Lock lock) {
        String holderIndexKey = buildHolderIndexKey(lock.getTenantId(), lock.getHolderId());
        redisTemplate.opsForSet().add(holderIndexKey, lock.getLockId().toString());
        redisTemplate.expire(holderIndexKey, 24, TimeUnit.HOURS);
    }

    private void removeFromHolderIndex(String tenantId, String holderId, String lockId) {
        String holderIndexKey = buildHolderIndexKey(tenantId, holderId);
        redisTemplate.opsForSet().remove(holderIndexKey, lockId);
    }

    private void updateLockStatistics(String tenantId, boolean acquired) {
        // Statistics are updated via RedisLockStatisticsRepository
        String statsKey = STATS_PREFIX + tenantId;
        if (acquired) {
            redisTemplate.opsForHash().increment(statsKey, "acquiredCount", 1);
        } else {
            redisTemplate.opsForHash().increment(statsKey, "failedCount", 1);
        }
        redisTemplate.expire(statsKey, 7, TimeUnit.DAYS);
    }
}
