package com.gogidix.shared.utilities.domain.port.out;

import com.gogidix.shared.utilities.domain.model.UtilityOperation;
import com.gogidix.shared.utilities.domain.model.OperationStatus;
import com.gogidix.shared.utilities.domain.model.UtilityType;
import com.gogidix.shared.utilities.domain.port.out.UtilityExecutor.ExecutionResult;
import com.gogidix.shared.utilities.domain.port.out.UtilityExecutor.ExecutionProgress;
import com.gogidix.shared.utilities.domain.port.out.UtilityExecutor.ParameterValidationResult;
import com.gogidix.shared.utilities.domain.port.out.CacheService.CacheStatistics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

class PortInterfaceTest {

    @Test
    void utilityOperationRepository_allMethods() {
        UtilityOperationRepository repo = new UtilityOperationRepository() {
            public UtilityOperation save(UtilityOperation op) { return op; }
            public Optional<UtilityOperation> findById(String id) { return Optional.empty(); }
            public List<UtilityOperation> findByUserId(String userId, int limit) { return List.of(); }
            public List<UtilityOperation> findByStatus(OperationStatus status, int limit) { return List.of(); }
            public List<UtilityOperation> findByType(UtilityType type, int limit) { return List.of(); }
            public List<UtilityOperation> findByUserIdAndStatus(String userId, OperationStatus status, int limit) { return List.of(); }
            public List<UtilityOperation> findByUserIdAndType(String userId, UtilityType type, int limit) { return List.of(); }
            public void deleteById(String id) {}
            public int deleteOlderThan(int maxAgeDays) { return 0; }
            public long countByStatus(OperationStatus status) { return 0; }
            public long countByUserId(String userId) { return 0; }
            public long countByUserIdAndStatus(String userId, OperationStatus status) { return 0; }
            public double getAverageExecutionTimeByUserId(String userId) { return 0.0; }
            public double getGlobalAverageExecutionTime() { return 0.0; }
            public boolean existsById(String id) { return false; }
            public UtilityOperation updateStatus(String id, OperationStatus status) { return null; }
            public UtilityOperation updateProgress(String id, int pct, String phase) { return null; }
        };

        assertNotNull(repo);
        assertFalse(repo.existsById("nonexistent"));
        assertEquals(0, repo.countByStatus(OperationStatus.COMPLETED));
        assertEquals(0.0, repo.getGlobalAverageExecutionTime());
        assertEquals(0, repo.deleteOlderThan(30));
        assertTrue(repo.findByUserId("user1", 10).isEmpty());
        assertTrue(repo.findByStatus(OperationStatus.RUNNING, 10).isEmpty());
        assertTrue(repo.findByType(UtilityType.JSON_PROCESSING, 10).isEmpty());
        assertTrue(repo.findByUserIdAndStatus("user1", OperationStatus.COMPLETED, 10).isEmpty());
        assertTrue(repo.findByUserIdAndType("user1", UtilityType.JSON_PROCESSING, 10).isEmpty());
        assertEquals(0, repo.countByUserId("user1"));
        assertEquals(0, repo.countByUserIdAndStatus("user1", OperationStatus.COMPLETED));
        assertEquals(0.0, repo.getAverageExecutionTimeByUserId("user1"));
        assertTrue(repo.findById("x").isEmpty());
    }

    @Test
    void utilityExecutor_allMethods() {
        UtilityExecutor exec = new UtilityExecutor() {
            public ExecutionResult execute(UtilityType type, Map<String, Object> params) {
                return ExecutionResult.success(Map.of("k", "v"), "exec-1", 100L);
            }
            public CompletableFuture<ExecutionResult> executeAsync(UtilityType type, Map<String, Object> params) {
                return CompletableFuture.completedFuture(ExecutionResult.success(Map.of(), "exec-2", 50L));
            }
            public boolean supports(UtilityType type) { return type == UtilityType.JSON_PROCESSING; }
            public ParameterValidationResult validateParameters(UtilityType type, Map<String, Object> params) {
                return ParameterValidationResult.valid();
            }
            public long getEstimatedExecutionTime(UtilityType type, Map<String, Object> params) { return 500L; }
            public boolean cancelExecution(String executionId) { return true; }
            public boolean pauseExecution(String executionId) { return true; }
            public boolean resumeExecution(String executionId) { return true; }
            public ExecutionProgress getProgress(String executionId) {
                return new ExecutionProgress(executionId, 50, "processing", 5, 10, "halfway", true, true);
            }
        };

        assertTrue(exec.supports(UtilityType.JSON_PROCESSING));
        assertFalse(exec.supports(UtilityType.CACHE_OPERATIONS));
        ExecutionResult result = exec.execute(UtilityType.JSON_PROCESSING, Map.of());
        assertTrue(result.isSuccess());
        assertEquals("exec-1", result.getExecutionId());
        assertEquals(100L, result.getExecutionTimeMs());

        ExecutionProgress progress = exec.getProgress("exec-1");
        assertEquals(50, progress.getProgressPercentage());
        assertEquals("processing", progress.getCurrentPhase());
        assertFalse(progress.isComplete());
        assertTrue(progress.canCancel());
        assertTrue(progress.canPause());

        ParameterValidationResult validation = exec.validateParameters(UtilityType.JSON_PROCESSING, Map.of());
        assertTrue(validation.isValid());
        assertFalse(validation.hasWarnings());

        assertEquals(500L, exec.getEstimatedExecutionTime(UtilityType.JSON_PROCESSING, Map.of()));
        assertTrue(exec.cancelExecution("exec-1"));
        assertTrue(exec.pauseExecution("exec-1"));
        assertTrue(exec.resumeExecution("exec-1"));
    }

    @Test
    void cacheService_allMethods() {
        CacheService cache = new CacheService() {
            public void put(String key, Object value, Duration ttl) {}
            public void put(String key, Object value) {}
            public <T> Optional<T> get(String key, Class<T> type) { return Optional.empty(); }
            public boolean exists(String key) { return false; }
            public void evict(String key) {}
            public void evictRegion(String region) {}
            public Optional<Duration> getTtl(String key) { return Optional.of(Duration.ofMinutes(5)); }
            public boolean extend(String key, Duration additionalTime) { return true; }
            public CacheStatistics getStatistics(String region) {
                return new CacheStatistics(region, 100L, 10L, 5L, 0.91, 85L);
            }
            public boolean isAvailable() { return true; }
        };

        assertTrue(cache.isAvailable());
        assertFalse(cache.exists("missing"));
        assertTrue(cache.get("missing", String.class).isEmpty());
        assertTrue(cache.getTtl("key").isPresent());
        assertTrue(cache.extend("key", Duration.ofMinutes(10)));

        CacheStatistics stats = cache.getStatistics("default");
        assertEquals("default", stats.getRegion());
        assertEquals(100L, stats.getHitCount());
        assertEquals(10L, stats.getMissCount());
        assertEquals(5L, stats.getEvictionCount());
        assertEquals(0.91, stats.getHitRate(), 0.01);
        assertEquals(85L, stats.getSize());
        assertEquals(110L, stats.getTotalRequests());
    }

    @Test
    void eventPublisher_allMethods() {
        EventPublisher pub = new EventPublisher() {
            public void publishOperationStarted(UtilityOperation op) {}
            public void publishOperationCompleted(UtilityOperation op) {}
            public void publishOperationFailed(UtilityOperation op, String errorMsg, String errorCode) {}
            public void publishOperationCancelled(UtilityOperation op, String reason) {}
            public void publishProgressUpdated(UtilityOperation op, int pct, String phase) {}
            public void publishStatusChanged(UtilityOperation op, OperationStatus oldStatus, OperationStatus newStatus) {}
            public void publishUtilityEvent(EventPublisher.UtilityEvent event) {}
        };

        assertNotNull(pub);
        pub.publishOperationStarted(null);
        pub.publishOperationCompleted(null);
        pub.publishOperationFailed(null, "error", "ERR001");
        pub.publishOperationCancelled(null, "user request");
        pub.publishProgressUpdated(null, 50, "processing");
        pub.publishStatusChanged(null, OperationStatus.RUNNING, OperationStatus.COMPLETED);
    }

    @Test
    void eventPublisher_utilityEventSubclasses() {
        LocalDateTime now = LocalDateTime.now();
        EventPublisher.OperationStartedEvent started = new EventPublisher.OperationStartedEvent(
            "evt-1", now, "op-1", "user-1", Map.of("detail", "test"));
        assertEquals("evt-1", started.getEventId());
        assertEquals("OPERATION_STARTED", started.getEventType());
        assertEquals("op-1", started.getOperationId());
        assertEquals("user-1", started.getUserId());

        EventPublisher.OperationCompletedEvent completed = new EventPublisher.OperationCompletedEvent(
            "evt-2", now, "op-2", "user-1", Map.of());
        assertEquals("OPERATION_COMPLETED", completed.getEventType());

        EventPublisher.OperationFailedEvent failed = new EventPublisher.OperationFailedEvent(
            "evt-3", now, "op-3", "user-1", Map.of());
        assertEquals("OPERATION_FAILED", failed.getEventType());

        EventPublisher.OperationCancelledEvent cancelled = new EventPublisher.OperationCancelledEvent(
            "evt-4", now, "op-4", "user-1", Map.of());
        assertEquals("OPERATION_CANCELLED", cancelled.getEventType());

        EventPublisher.ProgressUpdatedEvent progress = new EventPublisher.ProgressUpdatedEvent(
            "evt-5", now, "op-5", "user-1", Map.of());
        assertEquals("PROGRESS_UPDATED", progress.getEventType());

        EventPublisher.StatusChangedEvent statusChanged = new EventPublisher.StatusChangedEvent(
            "evt-6", now, "op-6", "user-1", Map.of());
        assertEquals("STATUS_CHANGED", statusChanged.getEventType());
    }

    @Test
    void executionResult_factoryMethods() {
        ExecutionResult success = ExecutionResult.success(
            Map.of("key", "value"), "exec-1", 200L);
        assertTrue(success.isSuccess());
        assertEquals("exec-1", success.getExecutionId());
        assertEquals(200L, success.getExecutionTimeMs());
        assertNull(success.getErrorMessage());
        assertNull(success.getErrorCode());
        assertNull(success.getError());

        ExecutionResult failure = ExecutionResult.failure(
            "exec-2", 100L, "something went wrong", "ERR001", new RuntimeException("test"));
        assertFalse(failure.isSuccess());
        assertEquals("exec-2", failure.getExecutionId());
        assertEquals("something went wrong", failure.getErrorMessage());
        assertEquals("ERR001", failure.getErrorCode());
        assertNotNull(failure.getError());
    }

    @Test
    void parameterValidationResult_factoryMethods() {
        ParameterValidationResult valid = ParameterValidationResult.valid();
        assertTrue(valid.isValid());
        assertTrue(valid.getErrors().isEmpty());
        assertFalse(valid.hasWarnings());

        ParameterValidationResult invalid = ParameterValidationResult.invalid(
            Map.of("param1", "required"));
        assertFalse(invalid.isValid());
        assertTrue(invalid.getErrors().containsKey("param1"));

        ParameterValidationResult withWarnings = ParameterValidationResult.validWithWarnings(
            Map.of("param2", "deprecated"));
        assertTrue(withWarnings.isValid());
        assertTrue(withWarnings.hasWarnings());
    }

    @Test
    void executionProgress_isComplete() {
        ExecutionProgress inProgress = new ExecutionProgress(
            "exec-1", 50, "half", 5, 10, "msg", true, true);
        assertFalse(inProgress.isComplete());

        ExecutionProgress complete = new ExecutionProgress(
            "exec-1", 100, "done", 10, 10, "msg", false, false);
        assertTrue(complete.isComplete());

        ExecutionProgress clamped = new ExecutionProgress(
            "exec-1", 150, "done", 10, 10, "msg", false, false);
        assertEquals(100, clamped.getProgressPercentage());
    }

    @Test
    void cacheStatistics_totalRequests() {
        CacheStatistics stats = new CacheStatistics(
            "region1", 80L, 20L, 5L, 0.8, 75L);
        assertEquals(100L, stats.getTotalRequests());
        assertEquals(0.8, stats.getHitRate(), 0.01);
    }
}
