package com.gogidix.shared.utilities.adapter.out.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.utilities.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

class PersistenceBranchTest {

    private UtilityOperationMapper mapper;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mapper = new UtilityOperationMapper(objectMapper);
    }

    @Test
    void toDomain_entityWithId() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setId(42L);
        entity.setOperationId("op-1");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setDescription("desc");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setInputData("{\"key\":\"val\"}");
        entity.setOutputData("{\"result\":\"ok\"}");
        entity.setProcessingContext("{\"ctx\":true}");
        entity.setOperationMetadata("{\"meta\":1}");
        entity.setErrorHistory("[\"err1\"]");

        UtilityOperation domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals("op-1", domain.getOperationId());
    }

    @Test
    void toDomain_entityWithNullId() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-2");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("RUNNING");
        entity.setName("test");
        entity.setPriority("HIGH");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");

        UtilityOperation domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals(OperationStatus.RUNNING, domain.getOperationStatus());
    }

    @Test
    void toDomain_deletedTrue() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-del");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("COMPLETED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setDeleted(true);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy("admin");
        entity.setDeletionReason("old");

        UtilityOperation domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertTrue(domain.isDeleted());
    }

    @Test
    void toDomain_nullRetryCount() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nc");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setRetryCount(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(0, domain.getRetryCount());
    }

    @Test
    void toDomain_nullMaxRetries() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nm");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setMaxRetries(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(3, domain.getMaxRetries());
    }

    @Test
    void toDomain_nullBackoffMultiplier() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nb");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setRetryBackoffMultiplier(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(1.5, domain.getRetryBackoffMultiplier());
    }

    @Test
    void toDomain_nullAllowPreemption() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-np");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setAllowPreemption(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertFalse(domain.isAllowPreemption());
    }

    @Test
    void toDomain_nullCached() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nc2");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setCached(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertFalse(domain.isCached());
    }

    @Test
    void toDomain_nullCacheWriteThrough() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nw");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setCacheWriteThrough(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertFalse(domain.isCacheWriteThrough());
    }

    @Test
    void toDomain_nullMaxMemory() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nmm");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setMaxMemoryBytes(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(0L, domain.getMaxMemoryBytes());
    }

    @Test
    void toDomain_nullActualMemory() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nam");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setActualMemoryUsedBytes(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(0L, domain.getActualMemoryUsedBytes());
    }

    @Test
    void toDomain_nullMaxCpuCores() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-ncpu");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setMaxCpuCores(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(4, domain.getMaxCpuCores());
    }

    @Test
    void toDomain_nullCpuUsage() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-ncu");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setCpuUsagePercent(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(0.0, domain.getCpuUsagePercent());
    }

    @Test
    void toDomain_nullMaxExecTime() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nmx");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setMaxExecutionTimeMs(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(300000L, domain.getMaxExecutionTimeMs());
    }

    @Test
    void toDomain_nullProgressPercentage() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-npp");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setProgressPercentage(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(0, domain.getProgressPercentage());
    }

    @Test
    void toDomain_nullItemsProcessed() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nip");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setItemsProcessed(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(0L, domain.getItemsProcessed());
    }

    @Test
    void toDomain_nullTotalItems() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-nti");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setTotalItemsToProcess(null);

        UtilityOperation domain = mapper.toDomain(entity);
        assertEquals(100L, domain.getTotalItemsToProcess());
    }

    @Test
    void toDomain_invalidJsonInputData() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-ij");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setInputData("not valid json {{{");

        UtilityOperation domain = mapper.toDomain(entity);
        assertNotNull(domain.getInputData());
        assertTrue(domain.getInputData().isEmpty());
    }

    @Test
    void toDomain_emptyStringJsonFields() {
        UtilityOperationEntity entity = new UtilityOperationEntity();
        entity.setOperationId("op-es");
        entity.setType("JSON_PROCESSING");
        entity.setStatus("QUEUED");
        entity.setName("test");
        entity.setPriority("NORMAL");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
        entity.setInputData("  ");
        entity.setOutputData("");
        entity.setProcessingContext("   ");
        entity.setOperationMetadata("");

        UtilityOperation domain = mapper.toDomain(entity);
        assertTrue(domain.getInputData().isEmpty());
        assertTrue(domain.getOutputData().isEmpty());
        assertTrue(domain.getProcessingContext().isEmpty());
        assertTrue(domain.getOperationMetadata().isEmpty());
    }

    @Test
    void toDomain_allStatuses() {
        for (OperationStatus status : OperationStatus.values()) {
            UtilityOperationEntity entity = new UtilityOperationEntity();
            entity.setOperationId("op-" + status.name());
            entity.setType("JSON_PROCESSING");
            entity.setStatus(status.name());
            entity.setName("test-" + status.name());
            entity.setPriority("NORMAL");
            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy("SYSTEM");
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy("SYSTEM");

            UtilityOperation domain = mapper.toDomain(entity);
            assertNotNull(domain);
        }
    }

    @Test
    void toDomain_allPriorities() {
        for (OperationPriority priority : OperationPriority.values()) {
            UtilityOperationEntity entity = new UtilityOperationEntity();
            entity.setOperationId("op-" + priority.name());
            entity.setType("JSON_PROCESSING");
            entity.setStatus("QUEUED");
            entity.setName("test-" + priority.name());
            entity.setPriority(priority.name());
            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy("SYSTEM");
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy("SYSTEM");

            UtilityOperation domain = mapper.toDomain(entity);
            assertNotNull(domain);
        }
    }

    @Test
    void toDomain_allTypes() {
        for (UtilityType type : UtilityType.values()) {
            UtilityOperationEntity entity = new UtilityOperationEntity();
            entity.setOperationId("op-" + type.name());
            entity.setType(type.name());
            entity.setStatus("QUEUED");
            entity.setName("test-" + type.name());
            entity.setPriority("NORMAL");
            entity.setCreatedAt(LocalDateTime.now());
            entity.setCreatedBy("SYSTEM");
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy("SYSTEM");

            UtilityOperation domain = mapper.toDomain(entity);
            assertNotNull(domain);
        }
    }

    @Test
    void toEntity_nullDeletedFields() {
        UtilityOperation op = new UtilityOperation(
            UUID.randomUUID(), 1L, LocalDateTime.now(), "system",
            null, null, false, null, null, null,
            "op-nd", UtilityType.JSON_PROCESSING, OperationStatus.QUEUED, "Test", "desc",
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            OperationPriority.NORMAL, 0, 3, 2.0, false,
            null, null, null, null, null,
            false, null, null, null, false,
            1024, 0, 2, 0.0, 30000L,
            0, "INIT", 0, 100, "standard"
        );
        UtilityOperationEntity entity = mapper.toEntity(op);
        assertNotNull(entity);
        assertFalse(entity.getDeleted());
        assertNull(entity.getDeletedAt());
        assertNull(entity.getDeletedBy());
        assertNull(entity.getDeletionReason());
    }
}
