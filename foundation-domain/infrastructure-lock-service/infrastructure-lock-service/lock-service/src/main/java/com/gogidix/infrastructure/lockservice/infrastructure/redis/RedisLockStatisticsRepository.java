package com.gogidix.infrastructure.lockservice.infrastructure.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.infrastructure.lockservice.domain.model.LockStatistics;
import com.gogidix.infrastructure.lockservice.domain.model.LockType;
import com.gogidix.infrastructure.lockservice.domain.repository.LockStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis-based implementation of LockStatisticsRepository.
 * Tracks lock metrics and provides monitoring data.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisLockStatisticsRepository implements LockStatisticsRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String STATS_PREFIX = "lock:stats:";
    private static final String GLOBAL_STATS_KEY = "lock:stats:global";
    private static final String ACQUISITION_TIMES_PREFIX = "lock:times:acquisition:";
    private static final String HOLD_TIMES_PREFIX = "lock:times:hold:";
    private static final String TENANT_LOCK_COUNTS_PREFIX = "lock:counts:tenant:";

    @Override
    public LockStatistics getStatistics(String tenantId) {
        String statsKey = STATS_PREFIX + tenantId;

        Map<Object, Object> statsHash = redisTemplate.opsForHash().entries(statsKey);

        return LockStatistics.builder()
            .tenantId(tenantId)
            .activeLocks(getLongValue(statsHash, "activeLocks"))
            .expiredLocks(getLongValue(statsHash, "expiredLocks"))
            .releasedLocks(getLongValue(statsHash, "releasedLocks"))
            .failedAttempts(getLongValue(statsHash, "failedAttempts"))
            .averageAcquisitionTimeMs(getAverageAcquisitionTime(tenantId))
            .averageHoldTimeMs(getAverageHoldTime(tenantId))
            .locksByType(getLocksByType(tenantId))
            .totalOperations(getLongValue(statsHash, "totalOperations"))
            .peakConcurrentLocks(getLongValue(statsHash, "peakConcurrentLocks"))
            .waitingRequests(getLongValue(statsHash, "waitingRequests"))
            .calculatedAt(LocalDateTime.now())
            .build();
    }

    @Override
    public LockStatistics getGlobalStatistics() {
        Map<Object, Object> globalStats = redisTemplate.opsForHash().entries(GLOBAL_STATS_KEY);

        // Aggregate from all tenant stats
        Set<String> tenantKeys = redisTemplate.keys(STATS_PREFIX + "*");
        long totalActiveLocks = 0;
        long totalExpiredLocks = 0;
        long totalReleasedLocks = 0;
        long totalFailedAttempts = 0;
        long totalOperations = 0;

        if (tenantKeys != null) {
            for (String key : tenantKeys) {
                Map<Object, Object> tenantStats = redisTemplate.opsForHash().entries(key);
                totalActiveLocks += getLongValue(tenantStats, "activeLocks");
                totalExpiredLocks += getLongValue(tenantStats, "expiredLocks");
                totalReleasedLocks += getLongValue(tenantStats, "releasedLocks");
                totalFailedAttempts += getLongValue(tenantStats, "failedAttempts");
                totalOperations += getLongValue(tenantStats, "totalOperations");
            }
        }

        return LockStatistics.builder()
            .tenantId("global")
            .activeLocks(totalActiveLocks)
            .expiredLocks(totalExpiredLocks)
            .releasedLocks(totalReleasedLocks)
            .failedAttempts(totalFailedAttempts)
            .totalOperations(totalOperations)
            .calculatedAt(LocalDateTime.now())
            .build();
    }

    @Override
    public Map<String, Long> getActiveLockCountByTenant() {
        Map<String, Long> result = new HashMap<>();
        Set<String> tenantKeys = redisTemplate.keys(STATS_PREFIX + "*");

        if (tenantKeys != null) {
            for (String key : tenantKeys) {
                String tenantId = key.substring(STATS_PREFIX.length());
                Map<Object, Object> stats = redisTemplate.opsForHash().entries(key);
                Long activeLocks = getLongValue(stats, "activeLocks");
                result.put(tenantId, activeLocks);
            }
        }

        return result;
    }

    @Override
    public void incrementFailedAttempts(String tenantId) {
        String statsKey = STATS_PREFIX + tenantId;
        redisTemplate.opsForHash().increment(statsKey, "failedAttempts", 1);
        redisTemplate.opsForHash().increment(GLOBAL_STATS_KEY, "failedAttempts", 1);
        redisTemplate.expire(statsKey, 7, TimeUnit.DAYS);
    }

    @Override
    public void incrementTotalOperations(String tenantId) {
        String statsKey = STATS_PREFIX + tenantId;
        redisTemplate.opsForHash().increment(statsKey, "totalOperations", 1);
        redisTemplate.opsForHash().increment(GLOBAL_STATS_KEY, "totalOperations", 1);
        redisTemplate.expire(statsKey, 7, TimeUnit.DAYS);
    }

    @Override
    public void recordAcquisitionTime(String tenantId, long timeMs) {
        String timesKey = ACQUISITION_TIMES_PREFIX + tenantId;
        redisTemplate.opsForList().rightPush(timesKey, String.valueOf(timeMs));
        // Keep only last 1000 records
        redisTemplate.opsForList().trim(timesKey, -1000, -1);
        redisTemplate.expire(timesKey, 1, TimeUnit.DAYS);
    }

    @Override
    public void recordHoldTime(String tenantId, long timeMs) {
        String timesKey = HOLD_TIMES_PREFIX + tenantId;
        redisTemplate.opsForList().rightPush(timesKey, String.valueOf(timeMs));
        redisTemplate.opsForList().trim(timesKey, -1000, -1);
        redisTemplate.expire(timesKey, 1, TimeUnit.DAYS);
    }

    @Override
    public void updatePeakConcurrentLocks(String tenantId, long currentCount) {
        String statsKey = STATS_PREFIX + tenantId;
        String peakKey = "peakConcurrentLocks";

        // Use Lua script to atomically update peak if current is higher
        String luaScript =
            "local key = KEYS[1]\n" +
            "local field = ARGV[1]\n" +
            "local current = tonumber(ARGV[2])\n" +
            "local existing = tonumber(redis.call('HGET', key, field) or '0')\n" +
            "if current > existing then\n" +
            "  redis.call('HSET', key, field, current)\n" +
            "  return 1\n" +
            "else\n" +
            "  return 0\n" +
            "end";

        redisTemplate.execute(
            (RedisCallback<Long>) connection -> {
                return connection.eval(
                    luaScript.getBytes(),
                    ReturnType.INTEGER,
                    1,
                    statsKey.getBytes(),
                    peakKey.getBytes(),
                    String.valueOf(currentCount).getBytes()
                );
            }
        );

        redisTemplate.expire(statsKey, 7, TimeUnit.DAYS);
    }

    // Public methods for updating statistics from other components

    /**
     * Increment the active lock count for a tenant.
     *
     * @param tenantId the tenant ID
     * @param lockType the type of lock
     */
    public void incrementActiveLocks(String tenantId, LockType lockType) {
        String statsKey = STATS_PREFIX + tenantId;
        redisTemplate.opsForHash().increment(statsKey, "activeLocks", 1);
        redisTemplate.opsForHash().increment(statsKey, "lockType:" + lockType.name(), 1);
        redisTemplate.opsForHash().increment(GLOBAL_STATS_KEY, "activeLocks", 1);
        redisTemplate.expire(statsKey, 7, TimeUnit.DAYS);
    }

    /**
     * Decrement the active lock count for a tenant.
     *
     * @param tenantId the tenant ID
     * @param lockType the type of lock
     */
    public void decrementActiveLocks(String tenantId, LockType lockType) {
        String statsKey = STATS_PREFIX + tenantId;
        redisTemplate.opsForHash().increment(statsKey, "activeLocks", -1);
        redisTemplate.opsForHash().increment(statsKey, "lockType:" + lockType.name(), -1);
        redisTemplate.opsForHash().increment(statsKey, "releasedLocks", 1);
        redisTemplate.opsForHash().increment(GLOBAL_STATS_KEY, "releasedLocks", 1);
        redisTemplate.expire(statsKey, 7, TimeUnit.DAYS);
    }

    /**
     * Increment expired lock count.
     *
     * @param tenantId the tenant ID
     */
    public void incrementExpiredLocks(String tenantId) {
        String statsKey = STATS_PREFIX + tenantId;
        redisTemplate.opsForHash().increment(statsKey, "expiredLocks", 1);
        redisTemplate.opsForHash().increment(GLOBAL_STATS_KEY, "expiredLocks", 1);
        redisTemplate.expire(statsKey, 7, TimeUnit.DAYS);
    }

    // Helper methods

    private Long getLongValue(Map<Object, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private Double getAverageAcquisitionTime(String tenantId) {
        return getAverageFromList(ACQUISITION_TIMES_PREFIX + tenantId);
    }

    private Double getAverageHoldTime(String tenantId) {
        return getAverageFromList(HOLD_TIMES_PREFIX + tenantId);
    }

    private Double getAverageFromList(String key) {
        List<String> values = redisTemplate.opsForList().range(key, 0, -1);
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        try {
            return values.stream()
                .mapToLong(Long::parseLong)
                .average()
                .orElse(0.0);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private Map<LockType, Long> getLocksByType(String tenantId) {
        String statsKey = STATS_PREFIX + tenantId;
        Map<Object, Object> statsHash = redisTemplate.opsForHash().entries(statsKey);

        Map<LockType, Long> result = new HashMap<>();
        for (LockType type : LockType.values()) {
            String key = "lockType:" + type.name();
            result.put(type, getLongValue(statsHash, key));
        }

        return result;
    }

    private interface RedisCallback<T> extends org.springframework.data.redis.core.RedisCallback<T> {
    }
}
