package com.gogidix.shared.utilities.application.service;

import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.*;
import com.gogidix.shared.utilities.domain.model.*;
import com.gogidix.shared.utilities.domain.port.out.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

class UtilityOperationServiceTest {

    private UtilityOperationService service;
    private UtilityOperationRepository repo;
    private UtilityExecutor executor;
    private CacheService cacheService;
    private EventPublisher eventPublisher;
    private Executor asyncExecutor;

    @BeforeEach
    void setUp() {
        repo = new StubRepo();
        executor = new StubExecutor();
        cacheService = new StubCacheService();
        eventPublisher = new StubEventPublisher();
        asyncExecutor = ForkJoinPool.commonPool();
        service = new UtilityOperationService(repo, executor, cacheService, eventPublisher, asyncExecutor);
    }

    @Test
    void executeOperation_success() {
        ExecuteOperationRequest req = createRequest(false);
        OperationResult result = service.executeOperation(req);
        assertTrue(result.isSuccess());
        assertEquals("Operation completed successfully", result.getMessage());
    }

    @Test
    void executeOperation_withCacheEnabled() {
        ExecuteOperationRequest req = createRequest(true);
        ((StubCacheService) cacheService).hasCachedResult = false;
        OperationResult result = service.executeOperation(req);
        assertTrue(result.isSuccess());
    }

    @Test
    void executeOperation_failure() {
        ((StubExecutor) executor).shouldFail = true;
        ExecuteOperationRequest req = createRequest(false);
        OperationResult result = service.executeOperation(req);
        assertFalse(result.isSuccess());
        assertEquals("ERR001", result.getErrorCode());
    }

    @Test
    void executeOperationAsync() throws Exception {
        ExecuteOperationRequest req = createRequest(false);
        CompletableFuture<OperationResult> future = service.executeOperationAsync(req);
        OperationResult result = future.get(10, TimeUnit.SECONDS);
        assertTrue(result.isSuccess());
    }

    @Test
    void executeBatchOperations() {
        List<ExecuteOperationRequest> requests = List.of(createRequest(false), createRequest(false));
        BatchOperationResult result = service.executeBatchOperations(requests);
        assertEquals(2, result.getTotalOperations());
        assertEquals(2, result.getSuccessfulOperations());
        assertEquals(0, result.getFailedOperations());
    }

    @Test
    void getOperation_found() {
        OperationResult result = service.executeOperation(createRequest(false));
        String opId = result.getOperation().getOperationId();
        assertTrue(service.getOperation(opId).isPresent());
    }

    @Test
    void getOperation_notFound() {
        assertTrue(service.getOperation("nonexistent").isEmpty());
    }

    @Test
    void getUserOperations() {
        service.executeOperation(createRequest(false));
        List<UtilityOperation> ops = service.getUserOperations("user1", 10);
        assertFalse(ops.isEmpty());
    }

    @Test
    void getOperationsByStatus() {
        service.executeOperation(createRequest(false));
        List<UtilityOperation> ops = service.getOperationsByStatus(OperationStatus.COMPLETED, 10);
        assertFalse(ops.isEmpty());
    }

    @Test
    void getOperationsByType() {
        service.executeOperation(createRequest(false));
        List<UtilityOperation> ops = service.getOperationsByType(UtilityType.JSON_PROCESSING, 10);
        assertFalse(ops.isEmpty());
    }

    @Test
    void cancelOperation_notFound() {
        OperationResult result = service.cancelOperation("nonexistent", "reason");
        assertFalse(result.isSuccess());
        assertEquals("NOT_FOUND", result.getErrorCode());
    }

    @Test
    void cancelOperation_success() {
        OperationResult execResult = service.executeOperation(createRequest(false));
        String opId = execResult.getOperation().getOperationId();
        OperationResult result = service.cancelOperation(opId, "user request");
        assertNotNull(result);
    }

    @Test
    void retryOperation_notFound() {
        OperationResult result = service.retryOperation("nonexistent");
        assertFalse(result.isSuccess());
        assertEquals("NOT_FOUND", result.getErrorCode());
    }

    @Test
    void pauseOperation_notFound() {
        OperationResult result = service.pauseOperation("nonexistent");
        assertFalse(result.isSuccess());
        assertEquals("NOT_FOUND", result.getErrorCode());
    }

    @Test
    void resumeOperation_notFound() {
        OperationResult result = service.resumeOperation("nonexistent");
        assertFalse(result.isSuccess());
        assertEquals("NOT_FOUND", result.getErrorCode());
    }

    @Test
    void getOperationStatistics() {
        service.executeOperation(createRequest(false));
        OperationStatistics stats = service.getOperationStatistics("user1");
        assertNotNull(stats);
        assertEquals("user1", stats.getUserId());
    }

    @Test
    void getGlobalStatistics() {
        service.executeOperation(createRequest(false));
        OperationStatistics stats = service.getGlobalStatistics();
        assertNotNull(stats);
        assertEquals("GLOBAL", stats.getUserId());
    }

    @Test
    void cleanupCompletedOperations() {
        service.executeOperation(createRequest(false));
        CleanupResult result = service.cleanupCompletedOperations(30);
        assertNotNull(result);
        assertTrue(result.getCleanupTimeMs() >= 0);
    }

    @Test
    void getAvailableUtilityTypes() {
        List<UtilityTypeInfo> types = service.getAvailableUtilityTypes();
        assertFalse(types.isEmpty());
        assertEquals(UtilityType.values().length, types.size());
    }

    @Test
    void validateOperationParameters_valid() {
        ValidationResult result = service.validateOperationParameters(UtilityType.JSON_PROCESSING, Map.of());
        assertTrue(result.isValid());
    }

    @Test
    void validateOperationParameters_invalid() {
        ((StubExecutor) executor).validationInvalid = true;
        ValidationResult result = service.validateOperationParameters(UtilityType.JSON_PROCESSING, Map.of());
        assertFalse(result.isValid());
    }

    @Test
    void validateOperationParameters_withWarnings() {
        ((StubExecutor) executor).validationWarnings = true;
        ValidationResult result = service.validateOperationParameters(UtilityType.JSON_PROCESSING, Map.of());
        assertTrue(result.isValid());
        assertTrue(result.hasWarnings());
    }

    @Test
    void getQueueStatus() {
        QueueStatus status = service.getQueueStatus();
        assertNotNull(status);
    }

    @Test
    void processQueue() {
        service.executeOperation(createRequest(false));
        ProcessingResult result = service.processQueue();
        assertNotNull(result);
        assertTrue(result.getProcessingTimeMs() >= 0);
    }

    private ExecuteOperationRequest createRequest(boolean cached) {
        return new ExecuteOperationRequest(
            UtilityType.JSON_PROCESSING, "test", "desc", Map.of("key", "val"),
            OperationPriority.NORMAL, "user1", "sess1", cached,
            cached ? "cache-key" : null, cached ? 300L : null, 60L, Map.of()
        );
    }

    static class StubRepo implements UtilityOperationRepository {
        private final Map<String, UtilityOperation> store = new ConcurrentHashMap<>();
        @Override public UtilityOperation save(UtilityOperation op) { store.put(op.getOperationId(), op); return op; }
        @Override public Optional<UtilityOperation> findById(String id) { return Optional.ofNullable(store.get(id)); }
        @Override public List<UtilityOperation> findByUserId(String userId, int limit) { return store.values().stream().filter(o -> userId.equals(o.getUserId())).limit(limit).toList(); }
        @Override public List<UtilityOperation> findByStatus(OperationStatus status, int limit) { return store.values().stream().filter(o -> o.getOperationStatus() == status).limit(limit).toList(); }
        @Override public List<UtilityOperation> findByType(UtilityType type, int limit) { return store.values().stream().filter(o -> o.getOperationType() == type).limit(limit).toList(); }
        @Override public List<UtilityOperation> findByUserIdAndStatus(String userId, OperationStatus status, int limit) { return List.of(); }
        @Override public List<UtilityOperation> findByUserIdAndType(String userId, UtilityType type, int limit) { return List.of(); }
        @Override public void deleteById(String id) { store.remove(id); }
        @Override public int deleteOlderThan(int maxAgeDays) { return 0; }
        @Override public long countByStatus(OperationStatus status) { return store.values().stream().filter(o -> o.getOperationStatus() == status).count(); }
        @Override public long countByUserId(String userId) { return store.values().stream().filter(o -> userId.equals(o.getUserId())).count(); }
        @Override public long countByUserIdAndStatus(String userId, OperationStatus status) { return 0; }
        @Override public double getAverageExecutionTimeByUserId(String userId) { return 150.0; }
        @Override public double getGlobalAverageExecutionTime() { return 200.0; }
        @Override public boolean existsById(String id) { return store.containsKey(id); }
        @Override public UtilityOperation updateStatus(String id, OperationStatus status) { return null; }
        @Override public UtilityOperation updateProgress(String id, int pct, String phase) { return null; }
    }

    static class StubExecutor implements UtilityExecutor {
        boolean shouldFail = false;
        boolean validationInvalid = false;
        boolean validationWarnings = false;
        @Override public ExecutionResult execute(UtilityType type, Map<String, Object> params) {
            if (shouldFail) return ExecutionResult.failure("exec-1", 100L, "error", "ERR001", null);
            return ExecutionResult.success(Map.of("result", "ok"), "exec-1", 100L);
        }
        @Override public CompletableFuture<ExecutionResult> executeAsync(UtilityType type, Map<String, Object> params) {
            return CompletableFuture.completedFuture(execute(type, params));
        }
        @Override public boolean supports(UtilityType type) { return true; }
        @Override public ParameterValidationResult validateParameters(UtilityType type, Map<String, Object> params) {
            if (validationInvalid) return ParameterValidationResult.invalid(Map.of("p1", "required"));
            if (validationWarnings) return ParameterValidationResult.validWithWarnings(Map.of("p1", "deprecated"));
            return ParameterValidationResult.valid();
        }
        @Override public long getEstimatedExecutionTime(UtilityType type, Map<String, Object> params) { return 500L; }
        @Override public boolean cancelExecution(String id) { return true; }
        @Override public boolean pauseExecution(String id) { return true; }
        @Override public boolean resumeExecution(String id) { return true; }
        @Override public ExecutionProgress getProgress(String id) { return new ExecutionProgress(id, 50, "half", 5, 10, "msg", true, true); }
    }

    static class StubCacheService implements CacheService {
        boolean hasCachedResult = false;
        @Override public void put(String key, Object value, Duration ttl) {}
        @Override public void put(String key, Object value) {}
        @Override public <T> Optional<T> get(String key, Class<T> type) {
            if (hasCachedResult) return Optional.of(type.cast(Map.of("cached", true)));
            return Optional.empty();
        }
        @Override public boolean exists(String key) { return false; }
        @Override public void evict(String key) {}
        @Override public void evictRegion(String region) {}
        @Override public Optional<Duration> getTtl(String key) { return Optional.empty(); }
        @Override public boolean extend(String key, Duration additionalTime) { return true; }
        @Override public CacheStatistics getStatistics(String region) { return new CacheStatistics(region, 0, 0, 0, 0, 0); }
        @Override public boolean isAvailable() { return true; }
    }

    static class StubEventPublisher implements EventPublisher {
        @Override public void publishOperationStarted(UtilityOperation op) {}
        @Override public void publishOperationCompleted(UtilityOperation op) {}
        @Override public void publishOperationFailed(UtilityOperation op, String errorMsg, String errorCode) {}
        @Override public void publishOperationCancelled(UtilityOperation op, String reason) {}
        @Override public void publishProgressUpdated(UtilityOperation op, int pct, String phase) {}
        @Override public void publishStatusChanged(UtilityOperation op, OperationStatus oldStatus, OperationStatus newStatus) {}
        @Override public void publishUtilityEvent(UtilityEvent event) {}
    }
}
