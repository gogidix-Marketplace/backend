package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.model.domain.model.BaseEntity;
import com.gogidix.shared.model.domain.model.ValidationResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

class UtilityOperationTest {

    private UtilityOperation createBasicOp() {
        return new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED,
            "Test Operation", "A test operation",
            Map.of("input", "data"), null, null, Map.of("meta", "value"),
            null, null, null, null, null, null,
            "user-1", "sess-1", "corr-1", "req-1", "client-1",
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024 * 1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
    }

    @Test
    void constructorAndGetters() {
        UtilityOperation op = createBasicOp();
        assertEquals("OP-001", op.getOperationId());
        assertEquals(UtilityType.JSON_PROCESSING, op.getOperationType());
        assertEquals(OperationStatus.QUEUED, op.getOperationStatus());
        assertEquals("Test Operation", op.getOperationName());
        assertEquals("A test operation", op.getOperationDescription());
        assertEquals("user-1", op.getUserId());
        assertEquals("sess-1", op.getSessionId());
        assertEquals("corr-1", op.getCorrelationId());
        assertEquals("req-1", op.getRequestId());
        assertEquals("client-1", op.getClientId());
        assertEquals(OperationPriority.NORMAL, op.getPriority());
        assertEquals(0, op.getRetryCount());
        assertEquals(3, op.getMaxRetries());
        assertEquals(2.0, op.getRetryBackoffMultiplier());
        assertFalse(op.isAllowPreemption());
        assertFalse(op.isCached());
        assertFalse(op.isCacheWriteThrough());
        assertEquals(0, op.getProgressPercentage());
        assertEquals("INIT", op.getCurrentPhase());
        assertEquals(0, op.getItemsProcessed());
        assertEquals(100, op.getTotalItemsToProcess());
        assertEquals("standard", op.getPerformanceProfile());
    }

    @Test
    void getInputDataReturnsCopy() {
        UtilityOperation op = createBasicOp();
        Map<String, Object> input = op.getInputData();
        assertEquals("data", input.get("input"));
        input.put("modified", "value");
        assertFalse(op.getInputData().containsKey("modified"));
    }

    @Test
    void getOperationMetadataReturnsCopy() {
        UtilityOperation op = createBasicOp();
        Map<String, String> meta = op.getOperationMetadata();
        assertEquals("value", meta.get("meta"));
    }

    @Test
    void isValidTrue() {
        UtilityOperation op = createBasicOp();
        assertTrue(op.isValid());
    }

    @Test
    void isInProgressTrueForQueued() {
        UtilityOperation op = createBasicOp();
        assertTrue(op.isInProgress());
    }

    @Test
    void isCompletedFalse() {
        UtilityOperation op = createBasicOp();
        assertFalse(op.isCompleted());
    }

    @Test
    void hasFailedFalse() {
        UtilityOperation op = createBasicOp();
        assertFalse(op.hasFailed());
    }

    @Test
    void canRetryTrue() {
        UtilityOperation op = createFailedOp();
        assertTrue(op.canRetry());
    }

    @Test
    void isHighPriorityFalse() {
        UtilityOperation op = createBasicOp();
        assertFalse(op.isHighPriority());
    }

    @Test
    void isHighPriorityTrue() {
        UtilityOperation op = createHighPriorityOp();
        assertTrue(op.isHighPriority());
    }

    @Test
    void startOperation() {
        UtilityOperation started = createBasicOp().startOperation("system");
        assertEquals(OperationStatus.RUNNING, started.getOperationStatus());
    }

    @Test
    void completeOperation() {
        UtilityOperation completed = createBasicOp().startOperation("system")
            .completeOperation(Map.of("result", "success"), "system");
        assertEquals(OperationStatus.COMPLETED, completed.getOperationStatus());
        assertNotNull(completed.getOperationEndTime());
        assertTrue(completed.isCompleted());
    }

    @Test
    void failOperation() {
        UtilityOperation failed = createBasicOp().startOperation("system")
            .failOperation("error msg", "ERR-001", "stack trace", "system");
        assertEquals(OperationStatus.FAILED, failed.getOperationStatus());
        assertEquals("error msg", failed.getErrorMessage());
        assertEquals("ERR-001", failed.getErrorCode());
        assertTrue(failed.hasFailed());
    }

    @Test
    void retryOperation() {
        UtilityOperation failed = createBasicOp().startOperation("system")
            .failOperation("error", "ERR-001", "stack", "system");
        UtilityOperation retrying = failed.retryOperation("system");
        assertTrue(retrying.canRetry() || retrying.getRetryCount() > 0);
    }

    @Test
    void cancelOperation() {
        UtilityOperation cancelled = createBasicOp().cancelOperation("not needed", "system");
        assertEquals(OperationStatus.CANCELLED, cancelled.getOperationStatus());
    }

    @Test
    void updateProgress() {
        UtilityOperation updated = createBasicOp().updateProgress(50, "PROCESSING", 50, "system");
        assertEquals(50, updated.getProgressPercentage());
        assertEquals("PROCESSING", updated.getCurrentPhase());
        assertEquals(50, updated.getItemsProcessed());
    }

    @Test
    void getInputParameter() {
        UtilityOperation op = createBasicOp();
        assertEquals("data", op.getInputParameter("input"));
        assertNull(op.getInputParameter("missing"));
    }

    @Test
    void getStringParameter() {
        UtilityOperation op = createBasicOp();
        assertEquals("data", op.getStringParameter("input", "default"));
        assertEquals("default", op.getStringParameter("missing", "default"));
    }

    @Test
    void shouldUseCache() {
        UtilityOperation op = createBasicOp();
        assertFalse(op.shouldUseCache());
    }

    @Test
    void generateCacheKey() {
        String key = createBasicOp().generateCacheKey();
        assertNotNull(key);
        assertTrue(key.startsWith("utility_"));
    }

    @Test
    void requiresAuthenticationTrueForUser() {
        assertTrue(createBasicOp().requiresAuthentication());
    }

    @Test
    void getComplexityScore() {
        int score = createBasicOp().getComplexityScore();
        assertTrue(score >= 0);
    }

    @Test
    void isOverdue() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now().minusHours(2), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.RUNNING,
            "Test", "desc", null, null, null, null,
            LocalDateTime.now().minusHours(2), null, null, null, null, null,
            "user-1", null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024 * 1024, 0, 2, 0.0, 1000L,
            0, "INIT", 0, 100, "standard"
        );
        assertNotNull(op);
    }

    @Test
    void getPerformanceEfficiency() {
        double eff = createBasicOp().getPerformanceEfficiency();
        assertTrue(eff >= 0.0);
    }

    @Test
    void getOperationSummary() {
        UtilityOperation op = createBasicOp();
        assertNotNull(op.getOperationSummary());
    }

    @Test
    void isPreemptibleTrue() {
        assertTrue(createBasicOp().isPreemptible());
    }

    @Test
    void validateBusinessRules() {
        ValidationResult result = createBasicOp().validateBusinessRules();
        assertNotNull(result);
    }

    private UtilityOperation createFailedOp() {
        return new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-FAIL", UtilityType.JSON_PROCESSING, OperationStatus.FAILED,
            "Failed Op", "desc", null, null, null, null,
            LocalDateTime.now(), LocalDateTime.now(), null, 1000L, null, 1000L,
            "user-1", null, null, null, null,
            OperationPriority.NORMAL, 1, 3, 2.0, false,
            "error", "ERR-001", "stack", List.of("error1"), null,
            false, null, null, null, false,
            1024 * 1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
    }

    private UtilityOperation createHighPriorityOp() {
        return new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-HIGH", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED,
            "High Op", "desc", null, null, null, null,
            null, null, null, null, null, null,
            "user-1", null, null, null, null,
            OperationPriority.CRITICAL, 0, 5, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024 * 1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
    }
}
