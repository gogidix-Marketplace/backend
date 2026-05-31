package com.gogidix.shared.utilities.adapter.out.persistence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class UtilityOperationEntityTest {

    @Test
    void gettersAndSetters() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setId(1L);
        entity.setOperationId("OP-001");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("Test Operation");
        entity.setDescription("A test");
        entity.setUserId("user-1");
        entity.setSessionId("sess-1");
        entity.setCorrelationId("corr-1");
        entity.setRequestId("req-1");
        entity.setClientId("client-1");
        entity.setPriority("NORMAL");
        entity.setRetryCount(0);
        entity.setMaxRetries(3);
        entity.setErrorMessage(null);
        entity.setErrorCode(null);
        entity.setInputData("{\"key\":\"value\"}");
        entity.setOutputData(null);
        entity.setCached(false);
        entity.setCacheKey(null);
        entity.setCacheTtlSeconds(null);
        entity.setScheduledTime(null);
        entity.setOperationStartTime(null);
        entity.setOperationEndTime(null);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(null);
        entity.setCreatedBy("system");
        entity.setUpdatedBy(null);
        entity.setProgressPercentage(0);
        entity.setCurrentPhase("INIT");

        assertEquals(1L, entity.getId());
        assertEquals("OP-001", entity.getOperationId());
        assertEquals("JSON_PROCESSING", entity.getType());
        assertEquals("QUEUED", entity.getStatus());
        assertEquals("Test Operation", entity.getName());
        assertEquals("A test", entity.getDescription());
        assertEquals("user-1", entity.getUserId());
        assertEquals("sess-1", entity.getSessionId());
        assertEquals("corr-1", entity.getCorrelationId());
        assertEquals("req-1", entity.getRequestId());
        assertEquals("client-1", entity.getClientId());
        assertEquals("NORMAL", entity.getPriority());
        assertEquals(0, entity.getRetryCount());
        assertEquals(3, entity.getMaxRetries());
        assertNull(entity.getErrorMessage());
        assertNull(entity.getErrorCode());
        assertEquals("{\"key\":\"value\"}", entity.getInputData());
        assertNull(entity.getOutputData());
        assertFalse(entity.getCached());
        assertNull(entity.getCacheKey());
        assertNull(entity.getCacheTtlSeconds());
        assertNotNull(entity.getCreatedAt());
        assertEquals("system", entity.getCreatedBy());
        assertEquals(0, entity.getProgressPercentage());
        assertEquals("INIT", entity.getCurrentPhase());
    }

    @Test
    void defaultValues() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        assertNull(entity.getId());
        assertNull(entity.getOperationId());
        assertEquals(0, entity.getRetryCount());
        assertEquals(3, entity.getMaxRetries());
        assertFalse(entity.getCached());
        assertEquals(0, entity.getProgressPercentage());
    }
}
