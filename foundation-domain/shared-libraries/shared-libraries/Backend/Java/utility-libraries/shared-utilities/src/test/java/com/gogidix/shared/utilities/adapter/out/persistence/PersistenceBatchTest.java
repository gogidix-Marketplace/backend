package com.gogidix.shared.utilities.adapter.out.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.utilities.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

class PersistenceBatchTest {

    private UtilityOperationMapper mapper;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mapper = new UtilityOperationMapper(objectMapper);
    }

    @Test
    void mapper_toEntity_null() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void mapper_toDomain_null() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void mapper_roundTrip() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        UtilityOperation op = new UtilityOperation(
            id, 1L, now, "SYSTEM", now, "SYSTEM",
            false, null, null, null,
            "util_test_123", UtilityType.JSON_PROCESSING, OperationStatus.COMPLETED,
            "Test Op", "Test Description",
            Map.of("input", "data"), Map.of("output", "result"),
            Map.of("ctx", "val"), Map.of("meta", "value"),
            now, now.plusSeconds(5), now, 5000L, 100L, 5100L,
            "user1", "sess1", "corr1", "req1", "client1",
            OperationPriority.HIGH, 0, 3, 1.5, false,
            null, null, null, List.of(), null,
            true, "cache-key", 300L, "default", false,
            1048576L, 512L, 4, 0.5, 300000L,
            100, "DONE", 50L, 100L, "standard"
        );

        UtilityOperationEntity entity = mapper.toEntity(op);
        assertNotNull(entity);
        assertEquals("util_test_123", entity.getOperationId());
        assertEquals("JSON_PROCESSING", entity.getType());
        assertEquals("COMPLETED", entity.getStatus());
        assertEquals("Test Op", entity.getName());
        assertEquals("Test Description", entity.getDescription());
        assertEquals("user1", entity.getUserId());
        assertEquals("sess1", entity.getSessionId());
        assertEquals("HIGH", entity.getPriority());

        UtilityOperation domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals("util_test_123", domain.getOperationId());
        assertEquals(UtilityType.JSON_PROCESSING, domain.getOperationType());
        assertEquals(OperationStatus.COMPLETED, domain.getOperationStatus());
        assertEquals("Test Op", domain.getOperationName());
        assertEquals("user1", domain.getUserId());
    }

    @Test
    void mapper_toDomain_nullFields() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-1");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setDescription(null);
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setInputData(null);
        entity.setOutputData(null);
        entity.setProcessingContext(null);
        entity.setOperationMetadata(null);
        entity.setErrorHistory(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals("op-1", domain.getOperationId());
        assertNotNull(domain.getInputData());
        assertTrue(domain.getInputData().isEmpty());
    }

    @Test
    void entity_allGettersSetters() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(1L);
        entity.setOperationId("op-1");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("COMPLETED");
        entity.setName("test");
        entity.setDescription("desc");
        entity.setUserId("user1");
        entity.setSessionId("sess1");
        entity.setCorrelationId("corr1");
        entity.setRequestId("req1");
        entity.setClientId("client1");
        entity.setPriority("HIGH");
        entity.setRetryCount(2);
        entity.setMaxRetries(5);
        entity.setRetryBackoffMultiplier(2.0);
        entity.setAllowPreemption(true);
        entity.setOperationStartTime(now);
        entity.setOperationEndTime(now.plusSeconds(10));
        entity.setScheduledTime(now);
        entity.setExecutionTimeMs(10000L);
        entity.setQueueTimeMs(200L);
        entity.setTotalProcessingTimeMs(10200L);
        entity.setErrorMessage("error msg");
        entity.setErrorCode("ERR001");
        entity.setErrorStackTrace("stack trace");
        entity.setRecoverySuggestion("retry");
        entity.setCached(true);
        entity.setCacheKey("cache-key");
        entity.setCacheTtlSeconds(600L);
        entity.setCacheRegion("region1");
        entity.setCacheWriteThrough(true);
        entity.setMaxMemoryBytes(1048576L);
        entity.setActualMemoryUsedBytes(512L);
        entity.setMaxCpuCores(8);
        entity.setCpuUsagePercent(75.5);
        entity.setMaxExecutionTimeMs(300000L);
        entity.setProgressPercentage(100);
        entity.setCurrentPhase("COMPLETE");
        entity.setItemsProcessed(100L);
        entity.setTotalItemsToProcess(100L);
        entity.setPerformanceProfile("fast");
        entity.setInputData("{\"key\":\"val\"}");
        entity.setOutputData("{\"result\":\"ok\"}");
        entity.setProcessingContext("{\"ctx\":true}");
        entity.setOperationMetadata("{\"meta\":1}");
        entity.setErrorHistory("[\"err1\"]");
        entity.setCreatedAt(now);
        entity.setCreatedBy("creator");
        entity.setUpdatedAt(now);
        entity.setUpdatedBy("updater");
        entity.setDeleted(true);
        entity.setDeletedAt(now);
        entity.setDeletedBy("admin");
        entity.setDeletionReason("old");
        entity.setVersion(2L);

        assertEquals(1L, entity.getId());
        assertEquals("op-1", entity.getOperationId());
        assertEquals("JSON_PROCESSING", entity.getType());
        assertEquals("COMPLETED", entity.getStatus());
        assertEquals("test", entity.getName());
        assertEquals("desc", entity.getDescription());
        assertEquals("user1", entity.getUserId());
        assertEquals("sess1", entity.getSessionId());
        assertEquals("corr1", entity.getCorrelationId());
        assertEquals("req1", entity.getRequestId());
        assertEquals("client1", entity.getClientId());
        assertEquals("HIGH", entity.getPriority());
        assertEquals(2, entity.getRetryCount());
        assertEquals(5, entity.getMaxRetries());
        assertEquals(2.0, entity.getRetryBackoffMultiplier());
        assertTrue(entity.getAllowPreemption());
        assertEquals(now, entity.getOperationStartTime());
        assertEquals(now.plusSeconds(10), entity.getOperationEndTime());
        assertEquals(now, entity.getScheduledTime());
        assertEquals(10000L, entity.getExecutionTimeMs());
        assertEquals(200L, entity.getQueueTimeMs());
        assertEquals(10200L, entity.getTotalProcessingTimeMs());
        assertEquals("error msg", entity.getErrorMessage());
        assertEquals("ERR001", entity.getErrorCode());
        assertEquals("stack trace", entity.getErrorStackTrace());
        assertEquals("retry", entity.getRecoverySuggestion());
        assertTrue(entity.getCached());
        assertEquals("cache-key", entity.getCacheKey());
        assertEquals(600L, entity.getCacheTtlSeconds());
        assertEquals("region1", entity.getCacheRegion());
        assertTrue(entity.getCacheWriteThrough());
        assertEquals(1048576L, entity.getMaxMemoryBytes());
        assertEquals(512L, entity.getActualMemoryUsedBytes());
        assertEquals(8, entity.getMaxCpuCores());
        assertEquals(75.5, entity.getCpuUsagePercent());
        assertEquals(300000L, entity.getMaxExecutionTimeMs());
        assertEquals(100, entity.getProgressPercentage());
        assertEquals("COMPLETE", entity.getCurrentPhase());
        assertEquals(100L, entity.getItemsProcessed());
        assertEquals(100L, entity.getTotalItemsToProcess());
        assertEquals("fast", entity.getPerformanceProfile());
        assertEquals("{\"key\":\"val\"}", entity.getInputData());
        assertEquals("{\"result\":\"ok\"}", entity.getOutputData());
        assertEquals("{\"ctx\":true}", entity.getProcessingContext());
        assertEquals("{\"meta\":1}", entity.getOperationMetadata());
        assertEquals("[\"err1\"]", entity.getErrorHistory());
        assertEquals(now, entity.getCreatedAt());
        assertEquals("creator", entity.getCreatedBy());
        assertEquals(now, entity.getUpdatedAt());
        assertEquals("updater", entity.getUpdatedBy());
        assertTrue(entity.getDeleted());
        assertEquals(now, entity.getDeletedAt());
        assertEquals("admin", entity.getDeletedBy());
        assertEquals("old", entity.getDeletionReason());
        assertEquals(2L, entity.getVersion());
    }

    @Test
    void entity_defaultValues() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        assertEquals(0, entity.getRetryCount());
        assertEquals(3, entity.getMaxRetries());
        assertEquals(1.5, entity.getRetryBackoffMultiplier());
        assertFalse(entity.getAllowPreemption());
        assertFalse(entity.getCached());
        assertFalse(entity.getCacheWriteThrough());
        assertFalse(entity.getDeleted());
        assertEquals(0, entity.getProgressPercentage());
        assertEquals(0L, entity.getItemsProcessed());
        assertEquals(100L, entity.getTotalItemsToProcess());
        assertEquals(1L, entity.getVersion());
    }
}
