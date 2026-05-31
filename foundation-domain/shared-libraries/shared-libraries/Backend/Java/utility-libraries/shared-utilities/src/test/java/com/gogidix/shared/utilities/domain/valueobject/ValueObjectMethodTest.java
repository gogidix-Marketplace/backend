package com.gogidix.shared.utilities.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValueObjectMethodTest {

    @Test
    void utilityType_allHaveDescriptions() {
        for (UtilityType type : UtilityType.values()) {
            assertNotNull(type.getDescription());
            assertFalse(type.getDescription().isEmpty());
        }
    }

    @Test
    void utilityType_category() {
        assertEquals("datetime", UtilityType.DATETIME_FORMATTING.getCategory());
        assertEquals("json", UtilityType.JSON_SERIALIZATION.getCategory());
        assertEquals("string", UtilityType.STRING_FORMATTING.getCategory());
        assertEquals("collection", UtilityType.COLLECTION_FILTERING.getCategory());
        assertEquals("file", UtilityType.FILE_EXCEL_PROCESSING.getCategory());
        assertEquals("http", UtilityType.HTTP_REQUEST_PROCESSING.getCategory());
        assertEquals("email", UtilityType.EMAIL_VALIDATION.getCategory());
        assertEquals("mathematical", UtilityType.MATHEMATICAL_CALCULATION.getCategory());
        assertEquals("general", UtilityType.HASHING.getCategory());
        assertEquals("object", UtilityType.OBJECT_INTROSPECTION.getCategory());
        assertEquals("utility", UtilityType.UTILITY_HEALTH_CHECK.getCategory());
    }

    @Test
    void utilityType_processingIntensive() {
        assertTrue(UtilityType.FILE_EXCEL_PROCESSING.isProcessingIntensive());
        assertTrue(UtilityType.FILE_CSV_PROCESSING.isProcessingIntensive());
        assertTrue(UtilityType.COLLECTION_AGGREGATION.isProcessingIntensive());
        assertTrue(UtilityType.MATHEMATICAL_CALCULATION.isProcessingIntensive());
        assertTrue(UtilityType.STATISTICAL_CALCULATION.isProcessingIntensive());
        assertTrue(UtilityType.ENCRYPTION.isProcessingIntensive());
        assertTrue(UtilityType.DECRYPTION.isProcessingIntensive());
        assertTrue(UtilityType.UTILITY_BATCH_PROCESSING.isProcessingIntensive());
        assertFalse(UtilityType.STRING_FORMATTING.isProcessingIntensive());
        assertFalse(UtilityType.DATETIME_FORMATTING.isProcessingIntensive());
    }

    @Test
    void utilityType_cacheable() {
        assertTrue(UtilityType.JSON_SCHEMA_VALIDATION.isCacheable());
        assertTrue(UtilityType.BUSINESS_RULE_VALIDATION.isCacheable());
        assertTrue(UtilityType.MATHEMATICAL_CALCULATION.isCacheable());
        assertTrue(UtilityType.STATISTICAL_CALCULATION.isCacheable());
        assertTrue(UtilityType.OBJECT_INTROSPECTION.isCacheable());
        assertTrue(UtilityType.CLASS_LOADING.isCacheable());
        assertFalse(UtilityType.STRING_FORMATTING.isCacheable());
        assertFalse(UtilityType.DATETIME_FORMATTING.isCacheable());
    }

    @Test
    void utilityType_critical() {
        assertTrue(UtilityType.DATA_INTEGRITY_VALIDATION.isCritical());
        assertTrue(UtilityType.BUSINESS_RULE_VALIDATION.isCritical());
        assertTrue(UtilityType.ENCRYPTION.isCritical());
        assertTrue(UtilityType.DECRYPTION.isCritical());
        assertTrue(UtilityType.FINANCIAL_CALCULATION.isCritical());
        assertFalse(UtilityType.STRING_FORMATTING.isCritical());
    }

    @Test
    void utilityType_requiresData() {
        assertFalse(UtilityType.UTILITY_HEALTH_CHECK.requiresData());
        assertFalse(UtilityType.UTILITY_PERFORMANCE_TEST.requiresData());
        assertTrue(UtilityType.STRING_FORMATTING.requiresData());
        assertTrue(UtilityType.DATETIME_FORMATTING.requiresData());
    }

    @Test
    void utilityType_sequentialProcessing() {
        assertTrue(UtilityType.FILE_EXCEL_PROCESSING.requiresSequentialProcessing());
        assertTrue(UtilityType.FILE_CSV_PROCESSING.requiresSequentialProcessing());
        assertTrue(UtilityType.UTILITY_BATCH_PROCESSING.requiresSequentialProcessing());
        assertFalse(UtilityType.STRING_FORMATTING.requiresSequentialProcessing());
    }

    @Test
    void utilityType_estimatedProcessingTime() {
        assertTrue(UtilityType.FILE_EXCEL_PROCESSING.getEstimatedProcessingTimeMs() > UtilityType.STRING_FORMATTING.getEstimatedProcessingTimeMs());
        assertEquals(5000L, UtilityType.ENCRYPTION.getEstimatedProcessingTimeMs());
        assertEquals(100L, UtilityType.JSON_SCHEMA_VALIDATION.getEstimatedProcessingTimeMs());
        assertEquals(500L, UtilityType.STRING_FORMATTING.getEstimatedProcessingTimeMs());
    }

    @Test
    void utilityType_resourceIntensity() {
        assertTrue(UtilityType.ENCRYPTION.getResourceIntensity() > UtilityType.STRING_FORMATTING.getResourceIntensity());
        assertEquals(8, UtilityType.FILE_EXCEL_PROCESSING.getResourceIntensity());
        assertEquals(6, UtilityType.DATA_INTEGRITY_VALIDATION.getResourceIntensity());
        assertEquals(3, UtilityType.STRING_FORMATTING.getResourceIntensity());
    }

    @Test
    void utilityType_priorityScore() {
        assertEquals(10, UtilityType.ENCRYPTION.getPriorityScore());
        assertEquals(10, UtilityType.FINANCIAL_CALCULATION.getPriorityScore());
        assertEquals(5, UtilityType.FILE_EXCEL_PROCESSING.getPriorityScore());
        assertEquals(3, UtilityType.STRING_FORMATTING.getPriorityScore());
    }

    @Test
    void processingStatus_successful() {
        assertTrue(ProcessingStatus.SUCCESS.isSuccessful());
        assertTrue(ProcessingStatus.SUCCESS_WITH_WARNINGS.isSuccessful());
        assertTrue(ProcessingStatus.PARTIAL_SUCCESS.isSuccessful());
        assertFalse(ProcessingStatus.FAILED.isSuccessful());
        assertFalse(ProcessingStatus.PENDING.isSuccessful());
    }

    @Test
    void processingStatus_failed() {
        assertTrue(ProcessingStatus.FAILED.isFailed());
        assertTrue(ProcessingStatus.CANCELLED.isFailed());
        assertTrue(ProcessingStatus.TIMEOUT.isFailed());
        assertFalse(ProcessingStatus.SUCCESS.isFailed());
        assertFalse(ProcessingStatus.PENDING.isFailed());
    }

    @Test
    void processingStatus_ongoing() {
        assertTrue(ProcessingStatus.PENDING.isOngoing());
        assertTrue(ProcessingStatus.IN_PROGRESS.isOngoing());
        assertFalse(ProcessingStatus.SUCCESS.isOngoing());
        assertFalse(ProcessingStatus.FAILED.isOngoing());
    }

    @Test
    void processingStatus_completed() {
        assertTrue(ProcessingStatus.SUCCESS.isCompleted());
        assertTrue(ProcessingStatus.FAILED.isCompleted());
        assertFalse(ProcessingStatus.PENDING.isCompleted());
        assertFalse(ProcessingStatus.IN_PROGRESS.isCompleted());
    }

    @Test
    void processingStatus_severity() {
        assertEquals("info", ProcessingStatus.SUCCESS.getSeverity());
        assertEquals("warning", ProcessingStatus.SUCCESS_WITH_WARNINGS.getSeverity());
        assertEquals("error", ProcessingStatus.FAILED.getSeverity());
        assertEquals("error", ProcessingStatus.TIMEOUT.getSeverity());
        assertEquals("warning", ProcessingStatus.CANCELLED.getSeverity());
        assertEquals("info", ProcessingStatus.PENDING.getSeverity());
        assertEquals("info", ProcessingStatus.PROCESSING.getSeverity());
        assertEquals("warning", ProcessingStatus.PARTIAL_SUCCESS.getSeverity());
    }

    @Test
    void processingStatus_allHaveDescriptions() {
        for (ProcessingStatus status : ProcessingStatus.values()) {
            assertNotNull(status.getDescription());
            assertFalse(status.getDescription().isEmpty());
        }
    }
}
