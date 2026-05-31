package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.utilities.domain.valueobject.ProcessingStatus;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

class UtilityResultCleanTest {

    @Test
    void constructorBasic() {
        UtilityResultClean<String> result = new UtilityResultClean<>(
            "op-1", UtilityType.JSON_SERIALIZATION, ProcessingStatus.SUCCESS,
            "test", null, null, null, Instant.now(), 100L, "node-1", 0, null);
        assertEquals("op-1", result.getOperationId());
        assertEquals(UtilityType.JSON_SERIALIZATION, result.getUtilityType());
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("test", result.getResult());
        assertEquals(100L, result.getProcessingTimeMs());
        assertEquals("node-1", result.getProcessingNode());
    }

    @Test
    void constructorRejectsNullUtilityType() {
        assertThrows(NullPointerException.class, () -> new UtilityResultClean<>(
            "op-1", null, ProcessingStatus.SUCCESS, "test", null, null, null,
            Instant.now(), 100L, "node-1", 0, null));
    }

    @Test
    void constructorRejectsNullStatus() {
        assertThrows(NullPointerException.class, () -> new UtilityResultClean<>(
            "op-1", UtilityType.JSON_SERIALIZATION, null, "test", null, null, null,
            Instant.now(), 100L, "node-1", 0, null));
    }

    @Test
    void isSuccessfulTrue() {
        UtilityResultClean<String> r = new UtilityResultClean<>(
            "op-1", UtilityType.JSON_SERIALIZATION, ProcessingStatus.SUCCESS,
            "test", null, null, null, Instant.now(), 0, null, 0, null);
        assertTrue(r.isSuccessful());
    }

    @Test
    void isSuccessfulWithWarnings() {
        UtilityResultClean<String> r = new UtilityResultClean<>(
            "op-1", UtilityType.JSON_SERIALIZATION, ProcessingStatus.SUCCESS_WITH_WARNINGS,
            "test", null, List.of("warn"), null, Instant.now(), 0, null, 0, null);
        assertTrue(r.isSuccessful());
        assertTrue(r.hasWarnings());
    }

    @Test
    void isFailedTrue() {
        UtilityResultClean<String> r = new UtilityResultClean<>(
            "op-1", UtilityType.JSON_SERIALIZATION, ProcessingStatus.FAILED,
            null, "error msg", null, null, Instant.now(), 0, null, 0, null);
        assertTrue(r.isFailed());
        assertEquals("error msg", r.getErrorMessage());
    }

    @Test
    void hasMetadata() {
        UtilityResultClean<String> r = new UtilityResultClean<>(
            "op-1", UtilityType.JSON_SERIALIZATION, ProcessingStatus.SUCCESS,
            "test", null, null, Map.of("key", "value"), Instant.now(), 0, null, 0, null);
        assertEquals(1, r.getMetadata().size());
    }

    @Test
    void processingTimeCappedAtZero() {
        UtilityResultClean<String> r = new UtilityResultClean<>(
            "op-1", UtilityType.JSON_SERIALIZATION, ProcessingStatus.SUCCESS,
            "test", null, null, null, Instant.now(), -100L, null, 0, null);
        assertEquals(0, r.getProcessingTimeMs());
    }

    @Test
    void retryCountCappedAtZero() {
        UtilityResultClean<String> r = new UtilityResultClean<>(
            "op-1", UtilityType.JSON_SERIALIZATION, ProcessingStatus.SUCCESS,
            "test", null, null, null, Instant.now(), 0, null, -5, null);
        assertEquals(0, r.getRetryCount());
    }

    @Test
    void nullCreatedAtDefaultsToProcessedAt() {
        Instant now = Instant.now();
        UtilityResultClean<String> r = new UtilityResultClean<>(
            "op-1", UtilityType.JSON_SERIALIZATION, ProcessingStatus.SUCCESS,
            "test", null, null, null, now, 0, null, 0, null);
        assertEquals(now, r.getCreatedAt());
    }
}
