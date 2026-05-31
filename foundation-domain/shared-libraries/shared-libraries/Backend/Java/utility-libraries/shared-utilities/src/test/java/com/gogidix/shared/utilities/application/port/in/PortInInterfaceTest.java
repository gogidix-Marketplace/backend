package com.gogidix.shared.utilities.application.port.in;

import com.gogidix.shared.utilities.domain.model.UtilityOperation;
import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.model.ProcessingRequest;
import com.gogidix.shared.utilities.domain.model.UtilityType;
import com.gogidix.shared.utilities.domain.model.OperationStatus;
import com.gogidix.shared.utilities.domain.model.OperationPriority;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.OperationResult;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.BatchOperationResult;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.OperationStatistics;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.CleanupResult;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.ValidationResult;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.QueueStatus;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.ProcessingResult;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.UtilityTypeInfo;
import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase.ExecuteOperationRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

class PortInInterfaceTest {

    @Test
    void processUtilityPort_allMethods() {
        ProcessUtilityPort port = new ProcessUtilityPort() {
            public UtilityResult<?> processUtilityRequest(ProcessingRequest request) {
                return UtilityResult.success("op-1", null, "ok");
            }
            public CompletableFuture<UtilityResult<?>> processUtilityRequestAsync(ProcessingRequest request) {
                return CompletableFuture.completedFuture(UtilityResult.success("op-1", null, "ok"));
            }
        };

        assertNotNull(port);
        UtilityResult<?> sync = port.processUtilityRequest(null);
        assertNotNull(sync);
        UtilityResult<?> async = port.processUtilityRequestAsync(null).join();
        assertNotNull(async);
    }

    @Test
    void utilityOperationUseCase_allMethods() {
        UtilityOperationUseCase useCase = new UtilityOperationUseCase() {
            public OperationResult executeOperation(ExecuteOperationRequest req) {
                return OperationResult.success(null, Map.of("result", "ok"), "done");
            }
            public CompletableFuture<OperationResult> executeOperationAsync(ExecuteOperationRequest req) {
                return CompletableFuture.completedFuture(
                    OperationResult.success(null, Map.of("result", "async"), "done"));
            }
            public BatchOperationResult executeBatchOperations(List<ExecuteOperationRequest> reqs) {
                return new BatchOperationResult(reqs.size(), reqs.size(), 0, List.of(), 100L);
            }
            public Optional<UtilityOperation> getOperation(String operationId) { return Optional.empty(); }
            public List<UtilityOperation> getUserOperations(String userId, int limit) { return List.of(); }
            public List<UtilityOperation> getOperationsByStatus(OperationStatus status, int limit) { return List.of(); }
            public List<UtilityOperation> getOperationsByType(UtilityType type, int limit) { return List.of(); }
            public OperationResult cancelOperation(String operationId, String reason) {
                return OperationResult.success(null, Map.of(), "cancelled");
            }
            public OperationResult retryOperation(String operationId) {
                return OperationResult.success(null, Map.of(), "retried");
            }
            public OperationResult pauseOperation(String operationId) {
                return OperationResult.success(null, Map.of(), "paused");
            }
            public OperationResult resumeOperation(String operationId) {
                return OperationResult.success(null, Map.of(), "resumed");
            }
            public OperationStatistics getOperationStatistics(String userId) {
                return new OperationStatistics(userId, 10, 8, 2, 150L, Map.of(), Map.of(), null);
            }
            public OperationStatistics getGlobalStatistics() {
                return new OperationStatistics(null, 100, 80, 20, 200L, Map.of(), Map.of(), null);
            }
            public CleanupResult cleanupCompletedOperations(int maxAge) {
                return new CleanupResult(5, 3, 50L, "cleaned");
            }
            public List<UtilityTypeInfo> getAvailableUtilityTypes() { return List.of(); }
            public ValidationResult validateOperationParameters(UtilityType type, Map<String, Object> params) {
                return ValidationResult.valid("all good");
            }
            public QueueStatus getQueueStatus() {
                return new QueueStatus(0, 1, 5, 0, 10.0, 4, 1);
            }
            public ProcessingResult processQueue() {
                return new ProcessingResult(3, 0, 50L, "processed 3");
            }
        };

        OperationResult execResult = useCase.executeOperation(null);
        assertNotNull(execResult);
        assertTrue(execResult.isSuccess());

        OperationResult asyncResult = useCase.executeOperationAsync(null).join();
        assertTrue(asyncResult.isSuccess());

        BatchOperationResult batch = useCase.executeBatchOperations(List.of());
        assertEquals(0, batch.getTotalOperations());
        assertEquals(0, batch.getSuccessfulOperations());

        assertTrue(useCase.getOperation("x").isEmpty());
        assertTrue(useCase.getUserOperations("u1", 10).isEmpty());
        assertTrue(useCase.getOperationsByStatus(OperationStatus.COMPLETED, 10).isEmpty());
        assertTrue(useCase.getOperationsByType(UtilityType.JSON_PROCESSING, 10).isEmpty());

        OperationResult cancel = useCase.cancelOperation("op1", "reason");
        assertTrue(cancel.isSuccess());
        assertTrue(useCase.retryOperation("op1").isSuccess());
        assertTrue(useCase.pauseOperation("op1").isSuccess());
        assertTrue(useCase.resumeOperation("op1").isSuccess());

        OperationStatistics stats = useCase.getOperationStatistics("u1");
        assertEquals("u1", stats.getUserId());
        assertEquals(10, stats.getTotalOperations());
        assertEquals(8, stats.getSuccessfulOperations());
        assertEquals(2, stats.getFailedOperations());
        assertEquals(0.8, stats.getSuccessRate(), 0.01);

        OperationStatistics global = useCase.getGlobalStatistics();
        assertEquals(100, global.getTotalOperations());

        CleanupResult cleanup = useCase.cleanupCompletedOperations(30);
        assertEquals(5, cleanup.getOperationsFound());
        assertEquals(3, cleanup.getOperationsRemoved());
        assertEquals(50L, cleanup.getCleanupTimeMs());

        assertTrue(useCase.getAvailableUtilityTypes().isEmpty());

        ValidationResult valid = useCase.validateOperationParameters(UtilityType.JSON_PROCESSING, Map.of());
        assertTrue(valid.isValid());
        assertFalse(valid.hasWarnings());

        QueueStatus queue = useCase.getQueueStatus();
        assertEquals(0, queue.getQueuedOperations());
        assertEquals(1, queue.getRunningOperations());
        assertEquals(5, queue.getTotalWorkers());
        assertFalse(queue.isOverloaded());

        ProcessingResult proc = useCase.processQueue();
        assertEquals(3, proc.getProcessedOperations());
        assertEquals(50L, proc.getProcessingTimeMs());
    }

    @Test
    void executeOperationRequest_getters() {
        ExecuteOperationRequest req = new ExecuteOperationRequest(
            UtilityType.JSON_PROCESSING, "test", "desc", Map.of("k", "v"),
            OperationPriority.HIGH, "user1", "sess1", true, "cache-key",
            300L, 60L, Map.of("meta", "val"));

        assertEquals(UtilityType.JSON_PROCESSING, req.getType());
        assertEquals("test", req.getName());
        assertEquals("desc", req.getDescription());
        assertEquals(1, req.getParameters().size());
        assertEquals(OperationPriority.HIGH, req.getPriority());
        assertEquals("user1", req.getUserId());
        assertEquals("sess1", req.getSessionId());
        assertTrue(req.isCached());
        assertEquals("cache-key", req.getCacheKey());
        assertEquals(300L, req.getCacheTtlSeconds());
        assertEquals(60L, req.getTimeoutSeconds());
        assertEquals(1, req.getMetadata().size());
    }

    @Test
    void operationResult_successAndFailure() {
        OperationResult success = OperationResult.success(null, Map.of("k", "v"), "ok");
        assertTrue(success.isSuccess());
        assertEquals("ok", success.getMessage());
        assertNull(success.getErrorCode());
        assertTrue(success.getError().isEmpty());

        OperationResult failure = OperationResult.failure(null, "fail", "ERR001", new RuntimeException("x"));
        assertFalse(failure.isSuccess());
        assertEquals("fail", failure.getMessage());
        assertEquals("ERR001", failure.getErrorCode());
        assertTrue(failure.getError().isPresent());
    }

    @Test
    void batchOperationResult_successRate() {
        BatchOperationResult result = new BatchOperationResult(10, 8, 2, List.of(), 500L);
        assertEquals(0.8, result.getSuccessRate(), 0.01);
        assertEquals(500L, result.getTotalExecutionTimeMs());
    }

    @Test
    void operationStatistics_successRate() {
        OperationStatistics stats = new OperationStatistics("u1", 0, 0, 0, 0L, Map.of(), Map.of(), null);
        assertEquals(0.0, stats.getSuccessRate(), 0.01);
    }

    @Test
    void cleanupResult_getters() {
        CleanupResult cr = new CleanupResult(10, 7, 100L, "cleaned 7");
        assertEquals(10, cr.getOperationsFound());
        assertEquals(7, cr.getOperationsRemoved());
        assertEquals(100L, cr.getCleanupTimeMs());
        assertEquals("cleaned 7", cr.getSummary());
    }

    @Test
    void utilityTypeInfo_getters() {
        UtilityTypeInfo info = new UtilityTypeInfo(
            UtilityType.JSON_PROCESSING, "JSON Processing", true, true,
            30L, 1048576L, true, List.of("input"));
        assertEquals(UtilityType.JSON_PROCESSING, info.getType());
        assertEquals("JSON Processing", info.getDisplayName());
        assertTrue(info.isCacheable());
        assertTrue(info.isRetryable());
        assertEquals(30L, info.getDefaultTimeoutSeconds());
        assertEquals(1048576L, info.getMaxInputSizeBytes());
        assertTrue(info.isRequiresAuthentication());
        assertEquals(1, info.getRequiredParameters().size());
    }

    @Test
    void validationResult_factoryMethods() {
        ValidationResult valid = ValidationResult.valid("ok");
        assertTrue(valid.isValid());
        assertFalse(valid.hasWarnings());
        assertTrue(valid.getErrors().isEmpty());

        ValidationResult invalid = ValidationResult.invalid(List.of("err1"), "bad");
        assertFalse(invalid.isValid());
        assertEquals(1, invalid.getErrors().size());

        ValidationResult withWarnings = ValidationResult.validWithWarnings(List.of("warn1"), "watch out");
        assertTrue(withWarnings.isValid());
        assertTrue(withWarnings.hasWarnings());
        assertEquals(1, withWarnings.getWarnings().size());
    }

    @Test
    void queueStatus_overloaded() {
        QueueStatus normal = new QueueStatus(2, 1, 5, 0, 10.0, 4, 1);
        assertFalse(normal.isOverloaded());
        assertEquals(5, normal.getTotalWorkers());

        QueueStatus overloaded = new QueueStatus(20, 1, 5, 0, 10.0, 4, 1);
        assertTrue(overloaded.isOverloaded());
    }

    @Test
    void processingResult_getters() {
        ProcessingResult pr = new ProcessingResult(5, 2, 100L, "processed 5");
        assertEquals(5, pr.getProcessedOperations());
        assertEquals(2, pr.getSkippedOperations());
        assertEquals(100L, pr.getProcessingTimeMs());
        assertEquals("processed 5", pr.getSummary());
    }
}
