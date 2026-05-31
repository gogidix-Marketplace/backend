package com.gogidix.shared.utilities.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnumBatchTest {

    @Test
    void operationPriorityValues() {
        assertEquals(4, OperationPriority.values().length);
        assertArrayEquals(
            new OperationPriority[]{OperationPriority.LOW, OperationPriority.NORMAL, OperationPriority.HIGH, OperationPriority.CRITICAL},
            OperationPriority.values()
        );
    }

    @Test
    void operationPriorityValueOf() {
        assertEquals(OperationPriority.LOW, OperationPriority.valueOf("LOW"));
        assertEquals(OperationPriority.CRITICAL, OperationPriority.valueOf("CRITICAL"));
    }

    @Test
    void operationPriorityGetters() {
        assertEquals("Low", OperationPriority.LOW.getDisplayName());
        assertEquals(1, OperationPriority.LOW.getLevel());
        assertEquals("Normal", OperationPriority.NORMAL.getDisplayName());
        assertEquals(2, OperationPriority.NORMAL.getLevel());
        assertEquals("High", OperationPriority.HIGH.getDisplayName());
        assertEquals(3, OperationPriority.HIGH.getLevel());
        assertEquals("Critical", OperationPriority.CRITICAL.getDisplayName());
        assertEquals(4, OperationPriority.CRITICAL.getLevel());
    }

    @Test
    void operationPriorityIsHigherThan() {
        assertTrue(OperationPriority.CRITICAL.isHigherThan(OperationPriority.HIGH));
        assertTrue(OperationPriority.HIGH.isHigherThan(OperationPriority.NORMAL));
        assertTrue(OperationPriority.NORMAL.isHigherThan(OperationPriority.LOW));
        assertFalse(OperationPriority.LOW.isHigherThan(OperationPriority.NORMAL));
        assertFalse(OperationPriority.NORMAL.isHigherThan(OperationPriority.NORMAL));
    }

    @Test
    void operationPriorityRequiresImmediateProcessing() {
        assertFalse(OperationPriority.LOW.requiresImmediateProcessing());
        assertFalse(OperationPriority.NORMAL.requiresImmediateProcessing());
        assertTrue(OperationPriority.HIGH.requiresImmediateProcessing());
        assertTrue(OperationPriority.CRITICAL.requiresImmediateProcessing());
    }

    @Test
    void operationPriorityGetProcessingDelayMs() {
        assertEquals(0, OperationPriority.CRITICAL.getProcessingDelayMs());
        assertEquals(100, OperationPriority.HIGH.getProcessingDelayMs());
        assertEquals(1000, OperationPriority.NORMAL.getProcessingDelayMs());
        assertEquals(5000, OperationPriority.LOW.getProcessingDelayMs());
    }

    @Test
    void operationPriorityGetQueueWeight() {
        assertEquals(1000, OperationPriority.CRITICAL.getQueueWeight());
        assertEquals(100, OperationPriority.HIGH.getQueueWeight());
        assertEquals(10, OperationPriority.NORMAL.getQueueWeight());
        assertEquals(1, OperationPriority.LOW.getQueueWeight());
    }

    @Test
    void operationPriorityGetTimeoutMultiplier() {
        assertEquals(2.0, OperationPriority.CRITICAL.getTimeoutMultiplier());
        assertEquals(1.5, OperationPriority.HIGH.getTimeoutMultiplier());
        assertEquals(1.0, OperationPriority.NORMAL.getTimeoutMultiplier());
        assertEquals(0.8, OperationPriority.LOW.getTimeoutMultiplier());
    }

    @Test
    void operationPriorityGetRetryCount() {
        assertEquals(5, OperationPriority.CRITICAL.getRetryCount());
        assertEquals(3, OperationPriority.HIGH.getRetryCount());
        assertEquals(2, OperationPriority.NORMAL.getRetryCount());
        assertEquals(1, OperationPriority.LOW.getRetryCount());
    }

    @Test
    void operationPriorityAllowsPreemption() {
        assertTrue(OperationPriority.CRITICAL.allowsPreemption());
        assertFalse(OperationPriority.HIGH.allowsPreemption());
        assertFalse(OperationPriority.NORMAL.allowsPreemption());
        assertFalse(OperationPriority.LOW.allowsPreemption());
    }

    @Test
    void operationPriorityGetResourceAllocationPercentage() {
        assertEquals(80, OperationPriority.CRITICAL.getResourceAllocationPercentage());
        assertEquals(60, OperationPriority.HIGH.getResourceAllocationPercentage());
        assertEquals(40, OperationPriority.NORMAL.getResourceAllocationPercentage());
        assertEquals(20, OperationPriority.LOW.getResourceAllocationPercentage());
    }

    @Test
    void operationPriorityShouldLog() {
        assertTrue(OperationPriority.LOW.shouldLog());
        assertTrue(OperationPriority.NORMAL.shouldLog());
        assertTrue(OperationPriority.HIGH.shouldLog());
        assertTrue(OperationPriority.CRITICAL.shouldLog());
    }

    @Test
    void operationPriorityGetLogLevel() {
        assertEquals("ERROR", OperationPriority.CRITICAL.getLogLevel());
        assertEquals("WARN", OperationPriority.HIGH.getLogLevel());
        assertEquals("INFO", OperationPriority.NORMAL.getLogLevel());
        assertEquals("INFO", OperationPriority.LOW.getLogLevel());
    }

    @Test
    void operationStatusValues() {
        assertEquals(9, OperationStatus.values().length);
    }

    @Test
    void operationStatusValueOf() {
        assertEquals(OperationStatus.QUEUED, OperationStatus.valueOf("QUEUED"));
        assertEquals(OperationStatus.RUNNING, OperationStatus.valueOf("RUNNING"));
        assertEquals(OperationStatus.COMPLETED, OperationStatus.valueOf("COMPLETED"));
        assertEquals(OperationStatus.FAILED, OperationStatus.valueOf("FAILED"));
    }

    @Test
    void operationStatusGetDisplayName() {
        assertEquals("Queued", OperationStatus.QUEUED.getDisplayName());
        assertEquals("Running", OperationStatus.RUNNING.getDisplayName());
        assertEquals("Completed", OperationStatus.COMPLETED.getDisplayName());
        assertEquals("Failed", OperationStatus.FAILED.getDisplayName());
        assertEquals("Error", OperationStatus.ERROR.getDisplayName());
        assertEquals("Cancelled", OperationStatus.CANCELLED.getDisplayName());
        assertEquals("Timeout", OperationStatus.TIMEOUT.getDisplayName());
        assertEquals("Paused", OperationStatus.PAUSED.getDisplayName());
        assertEquals("Retrying", OperationStatus.RETRYING.getDisplayName());
    }

    @Test
    void operationStatusIsActive() {
        assertTrue(OperationStatus.RUNNING.isActive());
        assertTrue(OperationStatus.RETRYING.isActive());
        assertFalse(OperationStatus.QUEUED.isActive());
        assertFalse(OperationStatus.COMPLETED.isActive());
        assertFalse(OperationStatus.FAILED.isActive());
    }

    @Test
    void operationStatusIsPending() {
        assertTrue(OperationStatus.QUEUED.isPending());
        assertTrue(OperationStatus.PAUSED.isPending());
        assertFalse(OperationStatus.RUNNING.isPending());
        assertFalse(OperationStatus.COMPLETED.isPending());
    }

    @Test
    void operationStatusIsFinished() {
        assertTrue(OperationStatus.COMPLETED.isFinished());
        assertTrue(OperationStatus.FAILED.isFinished());
        assertTrue(OperationStatus.ERROR.isFinished());
        assertTrue(OperationStatus.CANCELLED.isFinished());
        assertTrue(OperationStatus.TIMEOUT.isFinished());
        assertFalse(OperationStatus.RUNNING.isFinished());
        assertFalse(OperationStatus.QUEUED.isFinished());
    }

    @Test
    void operationStatusIsSuccess() {
        assertTrue(OperationStatus.COMPLETED.isSuccess());
        assertFalse(OperationStatus.FAILED.isSuccess());
        assertFalse(OperationStatus.RUNNING.isSuccess());
    }

    @Test
    void operationStatusIsFailure() {
        assertTrue(OperationStatus.FAILED.isFailure());
        assertTrue(OperationStatus.ERROR.isFailure());
        assertTrue(OperationStatus.TIMEOUT.isFailure());
        assertFalse(OperationStatus.COMPLETED.isFailure());
        assertFalse(OperationStatus.CANCELLED.isFailure());
    }

    @Test
    void operationStatusCanRetry() {
        assertTrue(OperationStatus.FAILED.canRetry());
        assertTrue(OperationStatus.ERROR.canRetry());
        assertTrue(OperationStatus.TIMEOUT.canRetry());
        assertFalse(OperationStatus.COMPLETED.canRetry());
        assertFalse(OperationStatus.CANCELLED.canRetry());
    }

    @Test
    void operationStatusCanCancel() {
        assertTrue(OperationStatus.QUEUED.canCancel());
        assertTrue(OperationStatus.RUNNING.canCancel());
        assertTrue(OperationStatus.PAUSED.canCancel());
        assertTrue(OperationStatus.RETRYING.canCancel());
        assertFalse(OperationStatus.COMPLETED.canCancel());
        assertFalse(OperationStatus.FAILED.canCancel());
    }

    @Test
    void operationStatusCanPause() {
        assertTrue(OperationStatus.RUNNING.canPause());
        assertTrue(OperationStatus.QUEUED.canPause());
        assertFalse(OperationStatus.PAUSED.canPause());
        assertFalse(OperationStatus.COMPLETED.canPause());
    }

    @Test
    void operationStatusCanResume() {
        assertTrue(OperationStatus.PAUSED.canResume());
        assertFalse(OperationStatus.RUNNING.canResume());
        assertFalse(OperationStatus.QUEUED.canResume());
    }

    @Test
    void operationStatusGetNextExpectedStatus() {
        assertEquals(OperationStatus.RUNNING, OperationStatus.QUEUED.getNextExpectedStatus());
        assertEquals(OperationStatus.COMPLETED, OperationStatus.RUNNING.getNextExpectedStatus());
        assertEquals(OperationStatus.RUNNING, OperationStatus.PAUSED.getNextExpectedStatus());
        assertEquals(OperationStatus.RUNNING, OperationStatus.RETRYING.getNextExpectedStatus());
        assertEquals(OperationStatus.COMPLETED, OperationStatus.COMPLETED.getNextExpectedStatus());
        assertEquals(OperationStatus.FAILED, OperationStatus.FAILED.getNextExpectedStatus());
    }

    @Test
    void operationStatusCanTransitionTo() {
        assertTrue(OperationStatus.QUEUED.canTransitionTo(OperationStatus.RUNNING));
        assertTrue(OperationStatus.QUEUED.canTransitionTo(OperationStatus.CANCELLED));
        assertTrue(OperationStatus.QUEUED.canTransitionTo(OperationStatus.PAUSED));
        assertFalse(OperationStatus.QUEUED.canTransitionTo(OperationStatus.COMPLETED));
        assertTrue(OperationStatus.RUNNING.canTransitionTo(OperationStatus.COMPLETED));
        assertTrue(OperationStatus.RUNNING.canTransitionTo(OperationStatus.FAILED));
        assertTrue(OperationStatus.RUNNING.canTransitionTo(OperationStatus.ERROR));
        assertTrue(OperationStatus.RUNNING.canTransitionTo(OperationStatus.CANCELLED));
        assertTrue(OperationStatus.RUNNING.canTransitionTo(OperationStatus.TIMEOUT));
        assertTrue(OperationStatus.RUNNING.canTransitionTo(OperationStatus.PAUSED));
        assertFalse(OperationStatus.RUNNING.canTransitionTo(OperationStatus.QUEUED));
        assertTrue(OperationStatus.PAUSED.canTransitionTo(OperationStatus.RUNNING));
        assertTrue(OperationStatus.PAUSED.canTransitionTo(OperationStatus.CANCELLED));
        assertTrue(OperationStatus.FAILED.canTransitionTo(OperationStatus.RETRYING));
        assertTrue(OperationStatus.ERROR.canTransitionTo(OperationStatus.CANCELLED));
        assertTrue(OperationStatus.RETRYING.canTransitionTo(OperationStatus.RUNNING));
        assertFalse(OperationStatus.COMPLETED.canTransitionTo(OperationStatus.RUNNING));
        assertFalse(OperationStatus.CANCELLED.canTransitionTo(OperationStatus.RUNNING));
    }

    @Test
    void operationStatusGetLogLevel() {
        assertEquals("INFO", OperationStatus.QUEUED.getLogLevel());
        assertEquals("INFO", OperationStatus.RUNNING.getLogLevel());
        assertEquals("INFO", OperationStatus.COMPLETED.getLogLevel());
        assertEquals("ERROR", OperationStatus.FAILED.getLogLevel());
        assertEquals("ERROR", OperationStatus.ERROR.getLogLevel());
        assertEquals("ERROR", OperationStatus.TIMEOUT.getLogLevel());
        assertEquals("WARN", OperationStatus.CANCELLED.getLogLevel());
        assertEquals("INFO", OperationStatus.PAUSED.getLogLevel());
        assertEquals("INFO", OperationStatus.RETRYING.getLogLevel());
    }

    @Test
    void utilityTypeModelValues() {
        assertEquals(30, UtilityType.values().length);
    }

    @Test
    void utilityTypeModelValueOf() {
        assertEquals(UtilityType.DATA_TRANSFORMATION, UtilityType.valueOf("DATA_TRANSFORMATION"));
        assertEquals(UtilityType.JSON_PROCESSING, UtilityType.valueOf("JSON_PROCESSING"));
        assertEquals(UtilityType.CUSTOM, UtilityType.valueOf("CUSTOM"));
    }

    @Test
    void utilityTypeModelGetters() {
        assertEquals("Data Transformation", UtilityType.DATA_TRANSFORMATION.getDisplayName());
        assertTrue(UtilityType.DATA_TRANSFORMATION.isCacheable());
        assertTrue(UtilityType.DATA_TRANSFORMATION.isRetryable());
        assertEquals(15, UtilityType.DATA_TRANSFORMATION.getComplexityWeight());
        assertFalse(UtilityType.ENCRYPTION.isCacheable());
        assertFalse(UtilityType.ENCRYPTION.isRetryable());
    }

    @Test
    void utilityTypeModelRequiresAuthentication() {
        assertTrue(UtilityType.ENCRYPTION.requiresAuthentication());
        assertTrue(UtilityType.DATABASE_OPERATIONS.requiresAuthentication());
        assertTrue(UtilityType.BACKUP_OPERATIONS.requiresAuthentication());
        assertTrue(UtilityType.SYSTEM_OPERATIONS.requiresAuthentication());
        assertTrue(UtilityType.EMAIL_OPERATIONS.requiresAuthentication());
        assertFalse(UtilityType.JSON_PROCESSING.requiresAuthentication());
        assertFalse(UtilityType.STRING_MANIPULATION.requiresAuthentication());
    }

    @Test
    void utilityTypeModelIsResourceIntensive() {
        assertTrue(UtilityType.FILE_PROCESSING.isResourceIntensive());
        assertTrue(UtilityType.IMAGE_PROCESSING.isResourceIntensive());
        assertTrue(UtilityType.BACKUP_OPERATIONS.isResourceIntensive());
        assertFalse(UtilityType.STRING_MANIPULATION.isResourceIntensive());
        assertFalse(UtilityType.DATE_TIME.isResourceIntensive());
    }

    @Test
    void utilityTypeModelSupportsBatchProcessing() {
        assertTrue(UtilityType.DATA_TRANSFORMATION.supportsBatchProcessing());
        assertTrue(UtilityType.JSON_PROCESSING.supportsBatchProcessing());
        assertTrue(UtilityType.VALIDATION.supportsBatchProcessing());
        assertFalse(UtilityType.ENCRYPTION.supportsBatchProcessing());
        assertFalse(UtilityType.EMAIL_OPERATIONS.supportsBatchProcessing());
    }

    @Test
    void utilityTypeModelGetDefaultTimeoutSeconds() {
        assertEquals(10, UtilityType.STRING_MANIPULATION.getDefaultTimeoutSeconds());
        assertEquals(30, UtilityType.JSON_PROCESSING.getDefaultTimeoutSeconds());
        assertEquals(60, UtilityType.CSV_PROCESSING.getDefaultTimeoutSeconds());
        assertEquals(120, UtilityType.DATA_TRANSFORMATION.getDefaultTimeoutSeconds());
        assertEquals(180, UtilityType.ENCRYPTION.getDefaultTimeoutSeconds());
        assertEquals(300, UtilityType.FILE_PROCESSING.getDefaultTimeoutSeconds());
        assertEquals(600, UtilityType.IMAGE_PROCESSING.getDefaultTimeoutSeconds());
        assertEquals(1800, UtilityType.BACKUP_OPERATIONS.getDefaultTimeoutSeconds());
    }

    @Test
    void utilityTypeModelGetMaxInputSizeBytes() {
        assertEquals(1024, UtilityType.STRING_MANIPULATION.getMaxInputSizeBytes());
        assertEquals(1048576, UtilityType.JSON_PROCESSING.getMaxInputSizeBytes());
        assertEquals(1073741824, UtilityType.SYSTEM_OPERATIONS.getMaxInputSizeBytes());
    }

    @Test
    void utilityTypeModelIsAuditable() {
        assertTrue(UtilityType.ENCRYPTION.isAuditable());
        assertTrue(UtilityType.DATABASE_OPERATIONS.isAuditable());
        assertTrue(UtilityType.BACKUP_OPERATIONS.isAuditable());
        assertFalse(UtilityType.JSON_PROCESSING.isAuditable());
        assertFalse(UtilityType.STRING_MANIPULATION.isAuditable());
    }

    @Test
    void utilityTypeModelGetPriorityAdjustment() {
        assertTrue(UtilityType.ENCRYPTION.getPriorityAdjustment() >= -1);
        assertTrue(UtilityType.FILE_PROCESSING.getPriorityAdjustment() <= 1);
        assertEquals(0, UtilityType.JSON_PROCESSING.getPriorityAdjustment());
        assertEquals(0, UtilityType.STRING_MANIPULATION.getPriorityAdjustment());
    }
}
