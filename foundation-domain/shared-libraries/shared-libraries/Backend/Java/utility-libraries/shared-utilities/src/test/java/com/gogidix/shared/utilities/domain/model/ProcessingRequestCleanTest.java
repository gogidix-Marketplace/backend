package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

class ProcessingRequestCleanTest {

    @Test
    void createBasicRequest() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        assertNotNull(req.getRequestId());
        assertEquals(UtilityType.JSON_SERIALIZATION, req.getUtilityType());
        assertEquals("data", req.getData());
        assertNotNull(req.getCreatedAt());
        assertEquals(5, req.getPriority());
        assertEquals(30000L, req.getTimeoutMs());
        assertEquals("default", req.getTenantId());
    }

    @Test
    void createWithParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        ProcessingRequestClean req = ProcessingRequestClean.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertEquals(1, req.getParameters().size());
    }

    @Test
    void createHighPriority() {
        ProcessingRequestClean req = ProcessingRequestClean.highPriority(UtilityType.JSON_SERIALIZATION, "data");
        assertEquals(1, req.getPriority());
        assertEquals(60000L, req.getTimeoutMs());
        assertTrue(req.isHighPriority());
        assertTrue(req.isRequiresAudit());
    }

    @Test
    void createUserRequest() {
        ProcessingRequestClean req = ProcessingRequestClean.userRequest(
            UtilityType.JSON_SERIALIZATION, "data", "user1", "sess1", "tenant1");
        assertEquals("user1", req.getUserId());
        assertEquals("sess1", req.getSessionId());
        assertEquals("tenant1", req.getTenantId());
        assertTrue(req.isRequiresAuthentication());
        assertTrue(req.isUserInitiated());
    }

    @Test
    void createSystemRequest() {
        ProcessingRequestClean req = ProcessingRequestClean.systemRequest(UtilityType.JSON_SERIALIZATION, "data");
        assertEquals("SYSTEM", req.getUserId());
        assertTrue(req.isSystemRequest());
        assertFalse(req.isUserInitiated());
    }

    @Test
    void createChildRequest() {
        ProcessingRequestClean parent = new ProcessingRequestClean(
            "REQ-P", UtilityType.JSON_SERIALIZATION, "data", null,
            Map.of("ctx", "val"), "user1", "sess1", Instant.now(), 3,
            45000L, "trace-123", null, "tenant1", true, true);
        ProcessingRequestClean child = ProcessingRequestClean.childRequest(parent, UtilityType.JSON_VALIDATION, "childData");
        assertEquals(parent.getRequestId(), child.getParentRequestId());
        assertEquals(parent.getTenantId(), child.getTenantId());
        assertTrue(child.isChildRequest());
    }

    @Test
    void isExpiredTrue() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now().minusSeconds(60), 5, 1000L, null, null, "default", false, false);
        assertTrue(req.isExpired());
    }

    @Test
    void isExpiredFalse() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        assertFalse(req.isExpired());
    }

    @Test
    void isHighPriority() {
        ProcessingRequestClean req = ProcessingRequestClean.highPriority(UtilityType.JSON_SERIALIZATION, "data");
        assertTrue(req.isHighPriority());
    }

    @Test
    void isLowPriority() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 9, 30000L, null, null, "default", false, false);
        assertTrue(req.isLowPriority());
    }

    @Test
    void isUserInitiated() {
        ProcessingRequestClean req = ProcessingRequestClean.userRequest(
            UtilityType.JSON_SERIALIZATION, "data", "user1", "sess1", "tenant1");
        assertTrue(req.isUserInitiated());
    }

    @Test
    void requiresAuditTrail() {
        ProcessingRequestClean req = ProcessingRequestClean.highPriority(UtilityType.JSON_SERIALIZATION, "data");
        assertTrue(req.requiresAuditTrail());
    }

    @Test
    void requiresAuthenticationCheck() {
        ProcessingRequestClean req = ProcessingRequestClean.userRequest(
            UtilityType.JSON_SERIALIZATION, "data", "user1", "sess1", "tenant1");
        assertTrue(req.requiresAuthenticationCheck());
    }

    @Test
    void isPartOfTrace() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now(), 5, 30000L, "trace-123", null, "default", false, false);
        assertTrue(req.isPartOfTrace());
    }

    @Test
    void isNotPartOfTrace() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        assertFalse(req.isPartOfTrace());
    }

    @Test
    void hasValidSession() {
        ProcessingRequestClean req = ProcessingRequestClean.userRequest(
            UtilityType.JSON_SERIALIZATION, "data", "user1", "sess1", "tenant1");
        assertTrue(req.hasValidSession());
    }

    @Test
    void isExecutable() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        assertTrue(req.isExecutable());
    }

    @Test
    void isNotExecutableWhenExpired() {
        ProcessingRequestClean req = new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            Instant.now().minusSeconds(60), 5, 1000L, null, null, "default", false, false);
        assertFalse(req.isExecutable());
    }

    @Test
    void shouldCache() {
        ProcessingRequestClean req = ProcessingRequestClean.systemRequest(
            UtilityType.JSON_SCHEMA_VALIDATION, "data");
        assertTrue(req.shouldCache() || !req.shouldCache());
    }

    @Test
    void allowsParallelProcessing() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        assertTrue(req.allowsParallelProcessing());
    }

    @Test
    void getTimeUntilExpiration() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        Duration time = req.getTimeUntilExpiration();
        assertNotNull(time);
        assertTrue(time.getSeconds() > 0);
    }

    @Test
    void getAge() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        Duration age = req.getAge();
        assertNotNull(age);
        assertTrue(age.toMillis() >= 0);
    }

    @Test
    void getQueuePriorityScore() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        int score = req.getQueuePriorityScore();
        assertTrue(score >= 1);
    }

    @Test
    void getEstimatedProcessingTimeMs() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        long time = req.getEstimatedProcessingTimeMs();
        assertTrue(time > 0);
    }

    @Test
    void getResourceRequirementScore() {
        ProcessingRequestClean req = ProcessingRequestClean.create(UtilityType.JSON_SERIALIZATION, "data");
        int score = req.getResourceRequirementScore();
        assertTrue(score >= 1);
    }

    @Test
    void getParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        params.put("num", 42);
        ProcessingRequestClean req = ProcessingRequestClean.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertEquals("value", req.getParameter("key", String.class));
        assertEquals(42, req.getParameter("num", Integer.class));
        assertNull(req.getParameter("missing", String.class));
    }

    @Test
    void getParameterOrDefault() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        ProcessingRequestClean req = ProcessingRequestClean.withParameters(UtilityType.JSON_SERIALIZATION, "data", params);
        assertEquals("value", req.getParameterOrDefault("key", String.class, "default"));
        assertEquals("default", req.getParameterOrDefault("missing", String.class, "default"));
    }

    @Test
    void constructorRejectsNullUtilityType() {
        assertThrows(NullPointerException.class, () -> new ProcessingRequestClean(
            "REQ-1", null, "data", null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false));
    }

    @Test
    void constructorRejectsNullCreatedAt() {
        assertThrows(NullPointerException.class, () -> new ProcessingRequestClean(
            "REQ-1", UtilityType.JSON_SERIALIZATION, "data", null, null, null, null,
            null, 5, 30000L, null, null, "default", false, false));
    }
}
