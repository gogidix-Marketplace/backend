package com.gogidix.shared.utilities.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValueObjectEnumBatchTest {

    @Test
    void processingStatusValues() {
        assertEquals(9, ProcessingStatus.values().length);
    }

    @Test
    void processingStatusValueOf() {
        assertEquals(ProcessingStatus.PENDING, ProcessingStatus.valueOf("PENDING"));
        assertEquals(ProcessingStatus.SUCCESS, ProcessingStatus.valueOf("SUCCESS"));
        assertEquals(ProcessingStatus.FAILED, ProcessingStatus.valueOf("FAILED"));
    }

    @Test
    void processingStatusGetDescription() {
        assertNotNull(ProcessingStatus.PENDING.getDescription());
        assertNotNull(ProcessingStatus.SUCCESS.getDescription());
        assertNotNull(ProcessingStatus.FAILED.getDescription());
    }

    @Test
    void processingStatusIsSuccessful() {
        assertTrue(ProcessingStatus.SUCCESS.isSuccessful());
        assertTrue(ProcessingStatus.SUCCESS_WITH_WARNINGS.isSuccessful());
        assertTrue(ProcessingStatus.PARTIAL_SUCCESS.isSuccessful());
        assertFalse(ProcessingStatus.FAILED.isSuccessful());
        assertFalse(ProcessingStatus.PENDING.isSuccessful());
    }

    @Test
    void processingStatusIsFailed() {
        assertTrue(ProcessingStatus.FAILED.isFailed());
        assertTrue(ProcessingStatus.CANCELLED.isFailed());
        assertTrue(ProcessingStatus.TIMEOUT.isFailed());
        assertFalse(ProcessingStatus.SUCCESS.isFailed());
        assertFalse(ProcessingStatus.PENDING.isFailed());
    }

    @Test
    void processingStatusIsOngoing() {
        assertTrue(ProcessingStatus.PENDING.isOngoing());
        assertTrue(ProcessingStatus.IN_PROGRESS.isOngoing());
        assertFalse(ProcessingStatus.SUCCESS.isOngoing());
        assertFalse(ProcessingStatus.FAILED.isOngoing());
    }

    @Test
    void processingStatusIsCompleted() {
        assertTrue(ProcessingStatus.SUCCESS.isCompleted());
        assertTrue(ProcessingStatus.FAILED.isCompleted());
        assertFalse(ProcessingStatus.PENDING.isCompleted());
        assertFalse(ProcessingStatus.IN_PROGRESS.isCompleted());
    }

    @Test
    void processingStatusGetSeverity() {
        assertEquals("info", ProcessingStatus.SUCCESS.getSeverity());
        assertEquals("warning", ProcessingStatus.SUCCESS_WITH_WARNINGS.getSeverity());
        assertEquals("warning", ProcessingStatus.PARTIAL_SUCCESS.getSeverity());
        assertEquals("error", ProcessingStatus.FAILED.getSeverity());
        assertEquals("error", ProcessingStatus.TIMEOUT.getSeverity());
        assertEquals("warning", ProcessingStatus.CANCELLED.getSeverity());
        assertEquals("info", ProcessingStatus.PENDING.getSeverity());
        assertEquals("info", ProcessingStatus.PROCESSING.getSeverity());
        assertEquals("info", ProcessingStatus.IN_PROGRESS.getSeverity());
    }

    @Test
    void utilityTypeValuesCount() {
        assertTrue(UtilityType.values().length > 40);
    }

    @Test
    void utilityTypeValueOf() {
        assertEquals(UtilityType.DATETIME_FORMATTING, UtilityType.valueOf("DATETIME_FORMATTING"));
        assertEquals(UtilityType.JSON_SERIALIZATION, UtilityType.valueOf("JSON_SERIALIZATION"));
        assertEquals(UtilityType.UTILITY_BATCH_PROCESSING, UtilityType.valueOf("UTILITY_BATCH_PROCESSING"));
    }

    @Test
    void utilityTypeGetDescription() {
        assertNotNull(UtilityType.DATETIME_FORMATTING.getDescription());
        assertNotNull(UtilityType.JSON_SERIALIZATION.getDescription());
    }

    @Test
    void utilityTypeGetCategory() {
        assertEquals("datetime", UtilityType.DATETIME_FORMATTING.getCategory());
        assertEquals("json", UtilityType.JSON_SERIALIZATION.getCategory());
        assertEquals("string", UtilityType.STRING_FORMATTING.getCategory());
    }

    @Test
    void utilityTypeIsProcessingIntensive() {
        assertTrue(UtilityType.FILE_EXCEL_PROCESSING.isProcessingIntensive());
        assertTrue(UtilityType.FILE_CSV_PROCESSING.isProcessingIntensive());
        assertTrue(UtilityType.COLLECTION_AGGREGATION.isProcessingIntensive());
        assertTrue(UtilityType.ENCRYPTION.isProcessingIntensive());
        assertTrue(UtilityType.DECRYPTION.isProcessingIntensive());
        assertTrue(UtilityType.UTILITY_BATCH_PROCESSING.isProcessingIntensive());
        assertFalse(UtilityType.DATETIME_FORMATTING.isProcessingIntensive());
        assertFalse(UtilityType.STRING_VALIDATION.isProcessingIntensive());
    }

    @Test
    void utilityTypeIsCacheable() {
        assertTrue(UtilityType.JSON_SCHEMA_VALIDATION.isCacheable());
        assertTrue(UtilityType.BUSINESS_RULE_VALIDATION.isCacheable());
        assertTrue(UtilityType.MATHEMATICAL_CALCULATION.isCacheable());
        assertFalse(UtilityType.DATETIME_FORMATTING.isCacheable());
        assertFalse(UtilityType.STRING_FORMATTING.isCacheable());
    }

    @Test
    void utilityTypeIsCritical() {
        assertTrue(UtilityType.DATA_INTEGRITY_VALIDATION.isCritical());
        assertTrue(UtilityType.ENCRYPTION.isCritical());
        assertTrue(UtilityType.DECRYPTION.isCritical());
        assertTrue(UtilityType.FINANCIAL_CALCULATION.isCritical());
        assertFalse(UtilityType.DATETIME_FORMATTING.isCritical());
        assertFalse(UtilityType.STRING_FORMATTING.isCritical());
    }

    @Test
    void utilityTypeRequiresData() {
        assertTrue(UtilityType.DATETIME_FORMATTING.requiresData());
        assertTrue(UtilityType.JSON_SERIALIZATION.requiresData());
        assertFalse(UtilityType.UTILITY_HEALTH_CHECK.requiresData());
        assertFalse(UtilityType.UTILITY_PERFORMANCE_TEST.requiresData());
    }

    @Test
    void utilityTypeRequiresSequentialProcessing() {
        assertTrue(UtilityType.FILE_EXCEL_PROCESSING.requiresSequentialProcessing());
        assertTrue(UtilityType.FILE_CSV_PROCESSING.requiresSequentialProcessing());
        assertTrue(UtilityType.UTILITY_BATCH_PROCESSING.requiresSequentialProcessing());
        assertFalse(UtilityType.DATETIME_FORMATTING.requiresSequentialProcessing());
    }

    @Test
    void utilityTypeGetEstimatedProcessingTimeMs() {
        assertTrue(UtilityType.FILE_EXCEL_PROCESSING.getEstimatedProcessingTimeMs() >= 5000L);
        assertTrue(UtilityType.JSON_SCHEMA_VALIDATION.getEstimatedProcessingTimeMs() <= 100L);
        assertTrue(UtilityType.DATETIME_FORMATTING.getEstimatedProcessingTimeMs() <= 500L);
    }

    @Test
    void utilityTypeGetResourceIntensity() {
        assertTrue(UtilityType.FILE_EXCEL_PROCESSING.getResourceIntensity() >= 8);
        assertTrue(UtilityType.DATA_INTEGRITY_VALIDATION.getResourceIntensity() >= 6);
        assertTrue(UtilityType.DATETIME_FORMATTING.getResourceIntensity() >= 1);
    }

    @Test
    void utilityTypeGetPriorityScore() {
        assertTrue(UtilityType.DATA_INTEGRITY_VALIDATION.getPriorityScore() >= 10);
        assertTrue(UtilityType.FILE_EXCEL_PROCESSING.getPriorityScore() >= 5);
        assertTrue(UtilityType.DATETIME_FORMATTING.getPriorityScore() >= 1);
    }
}
