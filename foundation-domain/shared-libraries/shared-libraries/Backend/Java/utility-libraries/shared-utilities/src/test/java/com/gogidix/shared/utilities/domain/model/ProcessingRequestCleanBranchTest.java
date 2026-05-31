package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ProcessingRequestCleanBranchTest {

    @Test
    void constructor_nullRequestId_throws() {
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            null, UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false));
    }

    @Test
    void constructor_emptyRequestId_throws() {
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            "  ", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false));
    }

    @Test
    void constructor_longRequestId_throws() {
        String longId = "x".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            longId, UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false));
    }

    @Test
    void constructor_nullPriority_defaultsTo5() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), null, 30000L, null, null, "default", false, false);
        assertEquals(5, req.getPriority());
    }

    @Test
    void constructor_invalidPriority_throws() {
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 0, 30000L, null, null, "default", false, false));
    }

    @Test
    void constructor_priorityAbove10_throws() {
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 11, 30000L, null, null, "default", false, false));
    }

    @Test
    void constructor_nullTimeout_defaultsTo30s() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, null, null, null, "default", false, false);
        assertEquals(30000L, req.getTimeoutMs());
    }

    @Test
    void constructor_timeoutBelowMin_throws() {
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 500L, null, null, "default", false, false));
    }

    @Test
    void constructor_timeoutAboveMax_throws() {
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 4000000L, null, null, "default", false, false));
    }

    @Test
    void constructor_emptyTenantId_defaultsToDefault() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "  ", false, false);
        assertEquals("default", req.getTenantId());
    }

    @Test
    void constructor_longTenantId_throws() {
        String longTenant = "x".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, longTenant, false, false));
    }

    @Test
    void constructor_authRequiredNoUser_throws() {
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", true, false));
    }

    @Test
    void constructor_childRequestWithoutTrace_throws() {
        assertThrows(IllegalArgumentException.class, () -> new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, "PARENT-1", "default", false, false));
    }

    @Test
    void constructor_childRequestWithTrace_ok() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, "trace-1", "PARENT-1", "default", false, false);
        assertTrue(req.isChildRequest());
        assertTrue(req.isPartOfTrace());
    }

    @Test
    void isExpired_notExpired() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        assertFalse(req.isExpired());
    }

    @Test
    void isLowPriority_false() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        assertFalse(req.isLowPriority());
    }

    @Test
    void requiresAuthenticationCheck_systemUser() {
        ProcessingRequestClean req = ProcessingRequestClean.systemRequest(UtilityType.JSON_SERIALIZATION, "data");
        assertFalse(req.requiresAuthenticationCheck());
    }

    @Test
    void hasValidSession_nullSession() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, "user1", null,
            Instant.now(), 3, 45000L, null, null, "tenant1", true, true);
        assertFalse(req.hasValidSession());
    }

    @Test
    void isExecutable_requiresAuthNoSession() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, "user1", null,
            Instant.now(), 3, 45000L, null, null, "tenant1", true, true);
        assertFalse(req.isExecutable());
    }

    @Test
    void isExecutable_nullDataRequiresData() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, null, null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        assertFalse(req.isExecutable());
    }

    @Test
    void shouldCache_cacheableType() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SCHEMA_VALIDATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        boolean result = req.shouldCache();
        assertNotNull(result);
    }

    @Test
    void allowsParallelProcessing_highPriority() {
        ProcessingRequestClean req = ProcessingRequestClean.highPriority(UtilityType.JSON_SERIALIZATION, "data");
        assertFalse(req.allowsParallelProcessing());
    }

    @Test
    void getTimeUntilExpiration_expired() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now().minusSeconds(60), 5, 1000L, null, null, "default", false, false);
        assertEquals(Duration.ZERO, req.getTimeUntilExpiration());
    }

    @Test
    void getEstimatedProcessingTimeMs_withLargeData() {
        String largeData = "x".repeat(5000);
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, largeData);
        long time = req.getEstimatedProcessingTimeMs();
        assertTrue(time > 0);
    }

    @Test
    void getEstimatedProcessingTimeMs_withByteData() {
        byte[] data = new byte[2048];
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, data);
        long time = req.getEstimatedProcessingTimeMs();
        assertTrue(time > 0);
    }

    @Test
    void getEstimatedProcessingTimeMs_withCollectionData() {
        List<String> data = List.of("a", "b", "c", "d", "e");
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, data);
        long time = req.getEstimatedProcessingTimeMs();
        assertTrue(time > 0);
    }

    @Test
    void getEstimatedProcessingTimeMs_withMapData() {
        Map<String, Object> data = Map.of("key1", "val1", "key2", "val2");
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, data);
        long time = req.getEstimatedProcessingTimeMs();
        assertTrue(time > 0);
    }

    @Test
    void getResourceRequirementScore_withLargeData() {
        Map<String, Object> data = Map.of("k", new byte[2 * 1024 * 1024]);
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, data);
        int score = req.getResourceRequirementScore();
        assertTrue(score >= 1);
    }

    @Test
    void getParameter_wrongType() {
        Map<String, Object> params = new HashMap<>();
        params.put("num", "not-a-number");
        ProcessingRequestClean req = ProcessingRequestClean.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertNull(req.getParameter("num", Integer.class));
    }

    @Test
    void getStringParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        ProcessingRequestClean req = ProcessingRequestClean.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertEquals("value", req.getStringParameter("key"));
    }

    @Test
    void getIntParameter_withDefault() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        assertEquals(42, req.getIntParameter("missing", 42));
    }

    @Test
    void getBooleanParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("flag", true);
        ProcessingRequestClean req = ProcessingRequestClean.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertTrue(req.getBooleanParameter("flag"));
    }

    @Test
    void getBooleanParameter_withDefault() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        assertFalse(req.getBooleanParameter("missing", false));
    }

    @Test
    void hasParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        ProcessingRequestClean req = ProcessingRequestClean.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertTrue(req.hasParameter("key"));
        assertFalse(req.hasParameter("missing"));
    }

    @Test
    void getContextValue_withDefault() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, Map.of("ip", "1.2.3.4"), null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        assertEquals("1.2.3.4", req.getContextValue("ip"));
        assertEquals("fallback", req.getContextValue("missing", "fallback"));
    }

    @Test
    void hasContextValue() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, Map.of("ip", "1.2.3.4"), null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        assertTrue(req.hasContextValue("ip"));
        assertFalse(req.hasContextValue("missing"));
    }

    @Test
    void getClientIp() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, Map.of("client_ip", "10.0.0.1"), null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        assertEquals("10.0.0.1", req.getClientIp());
    }

    @Test
    void getUserAgent() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, Map.of("user_agent", "Mozilla/5.0"), null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        assertEquals("Mozilla/5.0", req.getUserAgent());
    }

    @Test
    void getRequestSource() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, Map.of("source", "api"), null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        assertEquals("api", req.getRequestSource());
    }

    @Test
    void getRequestSource_default() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        assertEquals("unknown", req.getRequestSource());
    }

    @Test
    void withAdditionalParameters() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        ProcessingRequestClean modified = req.withAdditionalParameters(Map.of("newKey", "newVal"));
        assertEquals("newVal", modified.getParameter("newKey", String.class));
    }

    @Test
    void withAdditionalParameters_null() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        ProcessingRequestClean modified = req.withAdditionalParameters(null);
        assertTrue(modified.getParameters().isEmpty());
    }

    @Test
    void withAdditionalContext() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        ProcessingRequestClean modified = req.withAdditionalContext(Map.of("newCtx", "val"));
        assertEquals("val", modified.getContextValue("newCtx"));
    }

    @Test
    void withAdditionalContext_null() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        ProcessingRequestClean modified = req.withAdditionalContext(null);
        assertTrue(modified.getContext().isEmpty());
    }

    @Test
    void withPriority() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        ProcessingRequestClean modified = req.withPriority(1);
        assertEquals(1, modified.getPriority());
    }

    @Test
    void withTimeout() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        ProcessingRequestClean modified = req.withTimeout(60000L);
        assertEquals(60000L, modified.getTimeoutMs());
    }

    @Test
    void withTrace() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        ProcessingRequestClean modified = req.withTrace("trace-123");
        assertEquals("trace-123", modified.getTraceId());
        assertTrue(modified.isPartOfTrace());
    }

    @Test
    void equals_sameId() {
        ProcessingRequestClean req1 = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        ProcessingRequestClean req2 = new ProcessingRequestClean(
            "REQ-1", UtilityType.DATETIME_FORMATTING, "other", null, null, "user", "sess",
            Instant.now(), 1, 60000L, "trace", null, "tenant", true, true);
        assertEquals(req1, req2);
    }

    @Test
    void equals_differentId() {
        ProcessingRequestClean req1 = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        ProcessingRequestClean req2 = new ProcessingRequestClean(
            "REQ-2", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        assertNotEquals(req1, req2);
    }

    @Test
    void hashCode_sameId() {
        ProcessingRequestClean req1 = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false);
        ProcessingRequestClean req2 = new ProcessingRequestClean(
            "REQ-1", UtilityType.DATETIME_FORMATTING, "other", null, null, null, null,
            Instant.now(), 1, 60000L, null, null, "default", false, false);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void toString_containsInfo() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        String str = req.toString();
        assertTrue(str.contains("JSON_SERIALIZATION"));
    }
}
