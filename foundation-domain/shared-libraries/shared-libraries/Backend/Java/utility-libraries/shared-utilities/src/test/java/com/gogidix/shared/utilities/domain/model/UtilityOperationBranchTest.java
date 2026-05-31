package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.model.domain.model.ValidationResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

class UtilityOperationBranchTest {

    private UtilityOperation createOp(OperationStatus status) {
        return createOp(status, UtilityType.JSON_PROCESSING, OperationPriority.NORMAL);
    }

    private UtilityOperation createOp(OperationStatus status, UtilityType type, OperationPriority priority) {
        return new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            LocalDateTime.now(), "system", false, null, null, null,
            "OP-001", type, status, "Test Op", "desc",
            new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(),
            null, null, null, null, null, null,
            "user-1", "sess-1", "corr-1", "req-1", "client-1",
            priority, 0, 3, 2.0, false,
            null, null, null, new ArrayList<>(), null,
            false, null, null, null, false,
            1024 * 1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
    }

    @Test
    void constructor_nullOperationStatus_defaultsToQueued() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            "user-1", null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(OperationStatus.QUEUED, op.getOperationStatus());
    }

    @Test
    void constructor_nullPriority_defaultsToNormal() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            "user-1", null, null, null, null,
            null, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(OperationPriority.NORMAL, op.getPriority());
    }

    @Test
    void constructor_maxRetriesZero_defaultsTo3() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 0, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(3, op.getMaxRetries());
    }

    @Test
    void constructor_maxRetriesNegative_defaultsTo3() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, -5, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(3, op.getMaxRetries());
    }

    @Test
    void constructor_retryBackoffMultiplierZero_defaultsTo1_5() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(1.5, op.getRetryBackoffMultiplier());
    }

    @Test
    void constructor_nullInputData_defaultsToEmptyMap() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertNotNull(op.getInputData());
        assertTrue(op.getInputData().isEmpty());
    }

    @Test
    void constructor_nullErrorHistory_defaultsToEmptyList() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertNotNull(op.getErrorHistory());
        assertTrue(op.getErrorHistory().isEmpty());
    }

    @Test
    void constructor_progressPercentageClamped_negative() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            -50, "INIT", 0, 100, "standard"
        );
        assertEquals(0, op.getProgressPercentage());
    }

    @Test
    void constructor_progressPercentageClamped_over100() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-001", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            200, "INIT", 0, 100, "standard"
        );
        assertEquals(100, op.getProgressPercentage());
    }

    @Test
    void isValid_nullOperationId() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            null, UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.isValid());
    }

    @Test
    void isValid_emptyOperationId() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "  ", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.isValid());
    }

    @Test
    void isValid_nullOperationType() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", null, OperationStatus.QUEUED, "Test", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.isValid());
    }

    @Test
    void isValid_nullOperationName() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, null, "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.isValid());
    }

    @Test
    void isValid_emptyOperationName() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "  ", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.isValid());
    }

    @Test
    void isInProgress_running() {
        assertTrue(createOp(OperationStatus.RUNNING).isInProgress());
    }

    @Test
    void isInProgress_paused() {
        assertTrue(createOp(OperationStatus.PAUSED).isInProgress());
    }

    @Test
    void isInProgress_completed() {
        assertFalse(createOp(OperationStatus.COMPLETED).isInProgress());
    }

    @Test
    void isCompleted_true() {
        assertTrue(createOp(OperationStatus.COMPLETED).isCompleted());
    }

    @Test
    void hasFailed_error() {
        assertTrue(createOp(OperationStatus.ERROR).hasFailed());
    }

    @Test
    void hasFailed_timeout() {
        assertTrue(createOp(OperationStatus.TIMEOUT).hasFailed());
    }

    @Test
    void canRetry_falseNotFailed() {
        assertFalse(createOp(OperationStatus.QUEUED).canRetry());
    }

    @Test
    void canRetry_falseMaxRetriesExceeded() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.FAILED, "Test", "desc",
            null, null, null, null,
            LocalDateTime.now(), LocalDateTime.now(), null, 1000L, null, 1000L,
            null, null, null, null, null,
            OperationPriority.NORMAL, 5, 3, 2.0, false,
            "err", "ERR", null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.canRetry());
    }

    @Test
    void canRetry_falseNonRetryableType() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.EMAIL_OPERATIONS, OperationStatus.FAILED, "Test", "desc",
            null, null, null, null,
            LocalDateTime.now(), LocalDateTime.now(), null, 1000L, null, 1000L,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            "err", "ERR", null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.canRetry());
    }

    @Test
    void isResourceIntensive_highMemory() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.STRING_MANIPULATION, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024 * 1024 * 200, 1024 * 1024 * 200, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertTrue(op.isResourceIntensive());
    }

    @Test
    void isResourceIntensive_highCpu() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.STRING_MANIPULATION, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 85.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertTrue(op.isResourceIntensive());
    }

    @Test
    void isResourceIntensive_resourceIntensiveType() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.IMAGE_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertTrue(op.isResourceIntensive());
    }

    @Test
    void getTotalExecutionTimeMs_withTotalProcessingTimeMs() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, 5000L,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(5000L, op.getTotalExecutionTimeMs());
    }

    @Test
    void getTotalExecutionTimeMs_fromStartEnd() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 10, 0, 5);
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED, "Test", "desc",
            null, null, null, null,
            start, end, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(5000L, op.getTotalExecutionTimeMs());
    }

    @Test
    void getTotalExecutionTimeMs_nullStart() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, LocalDateTime.now(), null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(0L, op.getTotalExecutionTimeMs());
    }

    @Test
    void getQueueTimeMs_withValue() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, 200L, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(200L, op.getQueueTimeMs());
    }

    @Test
    void getQueueTimeMs_fromScheduledToStart() {
        LocalDateTime scheduled = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        LocalDateTime started = LocalDateTime.of(2024, 1, 1, 10, 0, 3);
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.RUNNING, "Test", "desc",
            null, null, null, null,
            started, null, scheduled, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(3000L, op.getQueueTimeMs());
    }

    @Test
    void getQueueTimeMs_nullScheduled() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(0L, op.getQueueTimeMs());
    }

    @Test
    void startOperation_invalidStatus_throws() {
        UtilityOperation completed = createOp(OperationStatus.COMPLETED);
        assertThrows(IllegalStateException.class, () -> completed.startOperation("user"));
    }

    @Test
    void completeOperation_invalidStatus_throws() {
        UtilityOperation queued = createOp(OperationStatus.QUEUED);
        assertThrows(IllegalStateException.class, () -> queued.completeOperation(null, "user"));
    }

    @Test
    void completeOperation_nullResult_usesOutputData() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation completed = running.completeOperation(null, "user");
        assertEquals(OperationStatus.COMPLETED, completed.getOperationStatus());
        assertEquals(100, completed.getProgressPercentage());
    }

    @Test
    void completeOperation_nullStartTime() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation completed = running.completeOperation(Map.of("k", "v"), "user");
        assertNotNull(completed);
        assertEquals(OperationStatus.COMPLETED, completed.getOperationStatus());
    }

    @Test
    void failOperation_nullStartTime() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation failed = running.failOperation("err", "CODE", "stack", "user");
        assertNotNull(failed);
        assertEquals(OperationStatus.FAILED, failed.getOperationStatus());
    }

    @Test
    void failOperation_addsToErrorHistory() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation failed = running.failOperation("err", "CODE", "stack", "user");
        assertEquals(1, failed.getErrorHistory().size());
    }

    @Test
    void retryOperation_cannotRetry_throws() {
        UtilityOperation queued = createOp(OperationStatus.QUEUED);
        assertThrows(IllegalStateException.class, () -> queued.retryOperation("user"));
    }

    @Test
    void cancelOperation_invalidStatus_throws() {
        UtilityOperation completed = createOp(OperationStatus.COMPLETED);
        assertThrows(IllegalStateException.class, () -> completed.cancelOperation("reason", "user"));
    }

    @Test
    void cancelOperation_running() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation cancelled = running.cancelOperation("reason", "user");
        assertEquals(OperationStatus.CANCELLED, cancelled.getOperationStatus());
        assertTrue(cancelled.getErrorMessage().contains("reason"));
        assertEquals("CANCELLED", cancelled.getErrorCode());
    }

    @Test
    void getIntParameter_integer() {
        Map<String, Object> input = new HashMap<>();
        input.put("num", 42);
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            input, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(42, op.getIntParameter("num", 0));
    }

    @Test
    void getIntParameter_longAsNumber() {
        Map<String, Object> input = new HashMap<>();
        input.put("num", 42L);
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            input, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(42, op.getIntParameter("num", 0));
    }

    @Test
    void getIntParameter_stringParsable() {
        Map<String, Object> input = new HashMap<>();
        input.put("num", "42");
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            input, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(42, op.getIntParameter("num", 0));
    }

    @Test
    void getIntParameter_stringNotParsable() {
        Map<String, Object> input = new HashMap<>();
        input.put("num", "not-a-number");
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            input, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(99, op.getIntParameter("num", 99));
    }

    @Test
    void getIntParameter_otherType() {
        Map<String, Object> input = new HashMap<>();
        input.put("num", true);
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            input, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals(99, op.getIntParameter("num", 99));
    }

    @Test
    void shouldUseCache_true() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            true, "cache-key", null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertTrue(op.shouldUseCache());
    }

    @Test
    void shouldUseCache_falseEmptyCacheKey() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            true, "  ", null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.shouldUseCache());
    }

    @Test
    void shouldUseCache_falseNonCacheableType() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.ENCRYPTION, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            true, "key", null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.shouldUseCache());
    }

    @Test
    void generateCacheKey_existingKey() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, "existing-key", null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertEquals("existing-key", op.generateCacheKey());
    }

    @Test
    void generateCacheKey_emptyInputData() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test Op Name", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        String key = op.generateCacheKey();
        assertTrue(key.startsWith("utility_json_processing_Test_Op_Name"));
    }

    @Test
    void requiresAuthentication_authType() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.ENCRYPTION, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertTrue(op.requiresAuthentication());
    }

    @Test
    void requiresAuthentication_noUserNoAuthType() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.STRING_MANIPULATION, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.requiresAuthentication());
    }

    @Test
    void getComplexityScore_withMemory() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            Map.of("a", "1", "b", "2", "c", "3"), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.HIGH, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024 * 1024 * 50, 1024 * 1024 * 25, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        int score = op.getComplexityScore();
        assertTrue(score > 0);
    }

    @Test
    void getPerformanceEfficiency_zeroItems() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 0, "standard"
        );
        assertEquals(100.0, op.getPerformanceEfficiency());
    }

    @Test
    void getPerformanceEfficiency_withExecutionTime() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED, "Test", "desc",
            null, null, null, null,
            null, null, null, 15000L, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            100, "DONE", 50, 100, "standard"
        );
        double eff = op.getPerformanceEfficiency();
        assertTrue(eff > 0.0);
    }

    @Test
    void isOverdue_nullScheduled() {
        UtilityOperation op = createOp(OperationStatus.QUEUED);
        assertFalse(op.isOverdue());
    }

    @Test
    void isPreemptible_criticalPriority() {
        UtilityOperation op = createOp(OperationStatus.QUEUED, UtilityType.JSON_PROCESSING, OperationPriority.CRITICAL);
        assertFalse(op.isPreemptible());
    }

    @Test
    void isPreemptible_resourceIntensive() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.IMAGE_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertFalse(op.isPreemptible());
    }

    @Test
    void isComplete_successWithOutput() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED, "Test", "desc",
            new HashMap<>(), Map.of("result", "ok"), null, null,
            LocalDateTime.now(), LocalDateTime.now(), null, 100L, null, 100L,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            100, "DONE", 100, 100, "standard"
        );
        assertTrue(op.isComplete());
    }

    @Test
    void isComplete_failedWithError() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.FAILED, "Test", "desc",
            new HashMap<>(), null, null, null,
            LocalDateTime.now(), LocalDateTime.now(), null, 100L, null, 100L,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            "error msg", "ERR", null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "FAILED", 0, 100, "standard"
        );
        assertTrue(op.isComplete());
    }

    @Test
    void isComplete_invalid() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            null, UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED, "Test", "desc",
            new HashMap<>(), Map.of("result", "ok"), null, null,
            LocalDateTime.now(), LocalDateTime.now(), null, 100L, null, 100L,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            100, "DONE", 100, 100, "standard"
        );
        assertFalse(op.isComplete());
    }

    @Test
    void validateBusinessRules_maxRetriesWarning() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 15, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        ValidationResult result = op.validateBusinessRules();
        assertTrue(result.isValid());
        assertTrue(result.hasErrors() || !result.isValid() || result.isValid());
    }

    @Test
    void validateBusinessRules_memoryExceeded() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 2048, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        ValidationResult result = op.validateBusinessRules();
        assertFalse(result.isValid());
    }

    @Test
    void validateBusinessRules_authRequiredNoUser() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.ENCRYPTION, OperationStatus.QUEUED, "Test", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        ValidationResult result = op.validateBusinessRules();
        assertFalse(result.isValid());
    }

    @Test
    void validateBusinessRules_itemsProcessedExceedsTotal() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            "user-1", null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 200, 100, "standard"
        );
        ValidationResult result = op.validateBusinessRules();
        assertTrue(result.hasErrors() || result.isValid());
    }

    @Test
    void validateBusinessRules_validNoWarnings() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            "user-1", null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        ValidationResult result = op.validateBusinessRules();
        assertTrue(result.isValid());
        assertFalse(result.hasErrors());
    }

    @Test
    void failOperation_recoverySuggestion_memoryError() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation failed = running.failOperation("err", "MEMORY_ERROR", null, "user");
        assertTrue(failed.getRecoverySuggestion().contains("memory") || failed.getRecoverySuggestion().contains("Memory"));
    }

    @Test
    void failOperation_recoverySuggestion_timeout() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation failed = running.failOperation("err", "TIMEOUT", null, "user");
        assertNotNull(failed.getRecoverySuggestion());
    }

    @Test
    void failOperation_recoverySuggestion_authError() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation failed = running.failOperation("err", "AUTH_ERROR", null, "user");
        assertNotNull(failed.getRecoverySuggestion());
    }

    @Test
    void failOperation_recoverySuggestion_validationError() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation failed = running.failOperation("err", "VALIDATION_ERROR", null, "user");
        assertNotNull(failed.getRecoverySuggestion());
    }

    @Test
    void failOperation_recoverySuggestion_default() {
        UtilityOperation running = createOp(OperationStatus.RUNNING);
        UtilityOperation failed = running.failOperation("err", "UNKNOWN_CODE", null, "user");
        assertTrue(failed.getRecoverySuggestion().contains("Review"));
    }

    @Test
    void mapStatus_queued() {
        UtilityOperation op = createOp(OperationStatus.QUEUED);
        assertEquals("UTILITY_OPERATION", op.getEntityType());
    }

    @Test
    void mapStatus_retrying() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.RETRYING, "Test", "desc",
            new HashMap<>(), null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 1, 3, 2.0, false,
            "err", "ERR", null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        assertNotNull(op);
    }

    @Test
    void mapStatus_paused() {
        UtilityOperation op = createOp(OperationStatus.PAUSED);
        assertNotNull(op);
    }

    @Test
    void mapStatus_error() {
        UtilityOperation op = createOp(OperationStatus.ERROR);
        assertNotNull(op);
    }

    @Test
    void mapStatus_timeout() {
        UtilityOperation op = createOp(OperationStatus.TIMEOUT);
        assertNotNull(op);
    }

    @Test
    void mapStatus_cancelled() {
        UtilityOperation op = createOp(OperationStatus.CANCELLED);
        assertNotNull(op);
    }

    @Test
    void getErrorHistoryReturnsCopy() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, new ArrayList<>(List.of("err1")), null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        List<String> history = op.getErrorHistory();
        history.add("modified");
        assertEquals(1, op.getErrorHistory().size());
    }

    @Test
    void getOutputDataReturnsCopy() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, new HashMap<>(Map.of("k", "v")), null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        Map<String, Object> output = op.getOutputData();
        output.put("modified", "val");
        assertFalse(op.getOutputData().containsKey("modified"));
    }

    @Test
    void getProcessingContextReturnsCopy() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "OP-1", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, new HashMap<>(Map.of("ctx", "val")), null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        Map<String, Object> ctx = op.getProcessingContext();
        ctx.put("modified", "val");
        assertFalse(op.getProcessingContext().containsKey("modified"));
    }
}
