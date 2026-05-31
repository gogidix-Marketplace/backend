package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.utilities.domain.valueobject.ProcessingStatus;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ModelBatchTest {

    @Test
    void utilityResultSuccess() {
        UtilityResult<String> result = UtilityResult.success("op-1", UtilityType.JSON_SERIALIZATION, "test");
        assertEquals("op-1", result.getOperationId());
        assertEquals(UtilityType.JSON_SERIALIZATION, result.getUtilityType());
        assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
        assertEquals("test", result.getResult());
        assertNull(result.getErrorMessage());
        assertNotNull(result.getProcessedAt());
    }

    @Test
    void utilityResultFailure() {
        UtilityResult<String> result = UtilityResult.failure("op-1", UtilityType.JSON_SERIALIZATION, "error msg");
        assertEquals(ProcessingStatus.FAILED, result.getStatus());
        assertNull(result.getResult());
        assertEquals("error msg", result.getErrorMessage());
    }

    @Test
    void utilityResultWithWarnings() {
        UtilityResult<String> result = UtilityResult.withWarnings("op-1", UtilityType.JSON_SERIALIZATION, "test", List.of("warn1"));
        assertEquals(ProcessingStatus.SUCCESS_WITH_WARNINGS, result.getStatus());
        assertEquals("test", result.getResult());
        assertEquals(1, result.getWarnings().size());
    }

    @Test
    void utilityResultIsSuccessful() {
        assertTrue(UtilityResult.success("op-1", UtilityType.JSON_SERIALIZATION, "x").isSuccessful());
        assertTrue(UtilityResult.withWarnings("op-1", UtilityType.JSON_SERIALIZATION, "x", List.of("w")).isSuccessful());
        assertFalse(UtilityResult.failure("op-1", UtilityType.JSON_SERIALIZATION, "e").isSuccessful());
    }

    @Test
    void utilityResultIsFailed() {
        assertTrue(UtilityResult.failure("op-1", UtilityType.JSON_SERIALIZATION, "e").isFailed());
        assertFalse(UtilityResult.success("op-1", UtilityType.JSON_SERIALIZATION, "x").isFailed());
    }

    @Test
    void utilityResultHasWarnings() {
        assertTrue(UtilityResult.withWarnings("op-1", UtilityType.JSON_SERIALIZATION, "x", List.of("w")).hasWarnings());
        assertFalse(UtilityResult.success("op-1", UtilityType.JSON_SERIALIZATION, "x").hasWarnings());
    }

    @Test
    void utilityResultBuilder() {
        UtilityResult<String> result = UtilityResult.<String>builder()
            .operationId("op-1")
            .utilityType(UtilityType.DATETIME_FORMATTING)
            .status(ProcessingStatus.PROCESSING)
            .result("test")
            .processingTimeMs(100L)
            .metadata(Map.of("key", "value"))
            .build();
        assertEquals("op-1", result.getOperationId());
        assertEquals(ProcessingStatus.PROCESSING, result.getStatus());
        assertEquals(100L, result.getProcessingTimeMs());
        assertEquals(1, result.getMetadata().size());
    }

    @Test
    void utilityResultToBuilder() {
        UtilityResult<String> original = UtilityResult.success("op-1", UtilityType.JSON_SERIALIZATION, "test");
        UtilityResult<String> modified = original.toBuilder().status(ProcessingStatus.FAILED).errorMessage("err").build();
        assertEquals(ProcessingStatus.FAILED, modified.getStatus());
        assertEquals("err", modified.getErrorMessage());
        assertEquals("op-1", modified.getOperationId());
    }

    @Test
    void processingRequestCreate() {
        ProcessingRequest req = ProcessingRequest.create(UtilityType.JSON_SERIALIZATION, "data");
        assertNotNull(req.getRequestId());
        assertTrue(req.getRequestId().startsWith("REQ-"));
        assertEquals(UtilityType.JSON_SERIALIZATION, req.getUtilityType());
        assertEquals("data", req.getData());
        assertNotNull(req.getCreatedAt());
        assertEquals(5, req.getPriority());
        assertEquals(30000L, req.getTimeoutMs());
    }

    @Test
    void processingRequestWithParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        ProcessingRequest req = ProcessingRequest.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertNotNull(req.getParameters());
        assertEquals(1, req.getParameters().size());
    }

    @Test
    void processingRequestHighPriority() {
        ProcessingRequest req = ProcessingRequest.highPriority(UtilityType.JSON_SERIALIZATION, "data");
        assertEquals(1, req.getPriority());
        assertEquals(60000L, req.getTimeoutMs());
        assertTrue(req.isHighPriority());
    }

    @Test
    void processingRequestGetParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        params.put("num", 42);
        ProcessingRequest req = ProcessingRequest.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertEquals("value", req.getParameter("key", String.class));
        assertEquals(42, req.getParameter("num", Integer.class));
        assertNull(req.getParameter("missing", String.class));
        assertNull(req.getParameter("key", Integer.class));
    }

    @Test
    void processingRequestGetParameterOrDefault() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        ProcessingRequest req = ProcessingRequest.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertEquals("value", req.getParameterOrDefault("key", String.class, "default"));
        assertEquals("default", req.getParameterOrDefault("missing", String.class, "default"));
    }

    @Test
    void processingRequestGetContextValue() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("REQ-1")
            .utilityType(UtilityType.JSON_SERIALIZATION)
            .data("data")
            .context(Map.of("tenant", "acme"))
            .build();
        assertEquals("acme", req.getContextValue("tenant"));
        assertNull(req.getContextValue("missing"));
    }

    @Test
    void processingRequestGetContextValueNull() {
        ProcessingRequest req = ProcessingRequest.create(UtilityType.JSON_SERIALIZATION, "data");
        assertNull(req.getContextValue("any"));
    }

    @Test
    void processingRequestIsExpired() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("REQ-1")
            .utilityType(UtilityType.JSON_SERIALIZATION)
            .data("data")
            .createdAt(Instant.now().minusSeconds(60))
            .timeoutMs(1000L)
            .build();
        assertTrue(req.isExpired());
    }

    @Test
    void processingRequestIsNotExpired() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("REQ-1")
            .utilityType(UtilityType.JSON_SERIALIZATION)
            .data("data")
            .createdAt(Instant.now())
            .timeoutMs(60000L)
            .build();
        assertFalse(req.isExpired());
    }

    @Test
    void processingRequestIsExpiredNullTimeout() {
        ProcessingRequest req = ProcessingRequest.builder()
            .requestId("REQ-1")
            .utilityType(UtilityType.JSON_SERIALIZATION)
            .data("data")
            .createdAt(Instant.now().minusSeconds(60))
            .build();
        assertFalse(req.isExpired());
    }

    @Test
    void processingRequestIsHighPriority() {
        ProcessingRequest high = ProcessingRequest.builder()
            .requestId("REQ-1")
            .utilityType(UtilityType.JSON_SERIALIZATION)
            .data("data")
            .priority(1)
            .build();
        assertTrue(high.isHighPriority());

        ProcessingRequest normal = ProcessingRequest.builder()
            .requestId("REQ-2")
            .utilityType(UtilityType.JSON_SERIALIZATION)
            .data("data")
            .priority(5)
            .build();
        assertFalse(normal.isHighPriority());
    }
}
