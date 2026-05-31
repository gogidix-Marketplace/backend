package com.gogidix.globalbusinessmanagement.countryingestion.application.dto;

import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.IngestionBatchDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IngestionBatchDtoTest {

        @Test
    void testBuilder() {
        IngestionBatchDto dto = IngestionBatchDto.builder()
                        .id("test-id")
            .batchId("test-batchId")
            .batchType(IngestionBatchDto.BatchTypeDto.FULL_IMPORT)
            .source("test-source")
            .sourceUrl("test-sourceUrl")
            .status(IngestionBatchDto.BatchStatusDto.PENDING)
            .startTime(LocalDateTime.of(2025,1,15,10,0))
            .endTime(LocalDateTime.of(2025,1,15,10,0))
            .totalRecords(42L)
            .processedRecords(42L)
            .successfulRecords(42L)
            .failedRecords(42L)
            .skippedRecords(42L)
            .progressPercentage(42)
            .schemaId("test-schemaId")
            .format("test-format")
            .fileSizeBytes(42L)
            .fileName("test-fileName")
            .fileChecksum("test-fileChecksum")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .createdDate(LocalDateTime.of(2025,1,15,10,0))
            .lastModifiedBy("test-lastModifiedBy")
            .lastModifiedDate(LocalDateTime.of(2025,1,15,10,0))
            .errorMessage("test-errorMessage")
            .errorMessages(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .statistics(Collections.emptyMap())
            .validationErrorCount(42L)
            .deleteOnCompletion(true)
            .completedBy("test-completedBy")
            .completedDate(LocalDateTime.of(2025,1,15,10,0))
            .archived(true)
            .archivedDate(LocalDateTime.of(2025,1,15,10,0))
            .retryCount(42)
            .maxRetries(42)
            .parentBatchId("test-parentBatchId")
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .validationEnabled(true)
            .stopOnError(true)
            .processingTimeMs(42L)
            .averageRecordProcessingTimeMs(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-batchId", dto.getBatchId());
        assertEquals(IngestionBatchDto.BatchTypeDto.FULL_IMPORT, dto.getBatchType());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-sourceUrl", dto.getSourceUrl());
        assertEquals(IngestionBatchDto.BatchStatusDto.PENDING, dto.getStatus());
        assertEquals(42L, dto.getTotalRecords());
        assertEquals(42L, dto.getProcessedRecords());
        assertEquals(42L, dto.getSuccessfulRecords());
        assertEquals(42L, dto.getFailedRecords());
        assertEquals(42L, dto.getSkippedRecords());
        assertEquals(42, dto.getProgressPercentage());
        assertEquals("test-schemaId", dto.getSchemaId());
        assertEquals("test-format", dto.getFormat());
        assertEquals(42L, dto.getFileSizeBytes());
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileChecksum", dto.getFileChecksum());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-createdByName", dto.getCreatedByName());
        assertEquals("test-lastModifiedBy", dto.getLastModifiedBy());
        assertEquals("test-errorMessage", dto.getErrorMessage());
        assertEquals(42L, dto.getValidationErrorCount());
        assertTrue(dto.getDeleteOnCompletion());
        assertEquals("test-completedBy", dto.getCompletedBy());
        assertTrue(dto.getArchived());
        assertEquals(42, dto.getRetryCount());
        assertEquals(42, dto.getMaxRetries());
        assertEquals("test-parentBatchId", dto.getParentBatchId());
        assertEquals("test-organizationId", dto.getOrganizationId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertTrue(dto.getValidationEnabled());
        assertTrue(dto.getStopOnError());
        assertEquals(42L, dto.getProcessingTimeMs());
    }

    @Test
    void testSettersAndGetters() {
        IngestionBatchDto dto = new IngestionBatchDto();
        dto.setId("val-id");
        dto.setBatchId("val-batchId");
        dto.setBatchType(IngestionBatchDto.BatchTypeDto.FULL_IMPORT);
        dto.setSource("val-source");
        dto.setSourceUrl("val-sourceUrl");
        dto.setStatus(IngestionBatchDto.BatchStatusDto.PENDING);
        dto.setProgressPercentage(99);
        dto.setSchemaId("val-schemaId");
        dto.setFormat("val-format");
        dto.setFileName("val-fileName");
        dto.setFileChecksum("val-fileChecksum");
        dto.setCreatedBy("val-createdBy");
        dto.setCreatedByName("val-createdByName");
        dto.setLastModifiedBy("val-lastModifiedBy");
        dto.setErrorMessage("val-errorMessage");
        dto.setDeleteOnCompletion(true);
        dto.setCompletedBy("val-completedBy");
        dto.setArchived(true);
        dto.setRetryCount(99);
        dto.setMaxRetries(99);
        dto.setParentBatchId("val-parentBatchId");
        dto.setOrganizationId("val-organizationId");
        dto.setTenantId("val-tenantId");
        dto.setValidationEnabled(true);
        dto.setStopOnError(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals(IngestionBatchDto.BatchTypeDto.FULL_IMPORT, dto.getBatchType());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-sourceUrl", dto.getSourceUrl());
        assertEquals(IngestionBatchDto.BatchStatusDto.PENDING, dto.getStatus());
        assertEquals(99, dto.getProgressPercentage());
        assertEquals("val-schemaId", dto.getSchemaId());
        assertEquals("val-format", dto.getFormat());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileChecksum", dto.getFileChecksum());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-createdByName", dto.getCreatedByName());
        assertEquals("val-lastModifiedBy", dto.getLastModifiedBy());
        assertEquals("val-errorMessage", dto.getErrorMessage());
        assertTrue(dto.getDeleteOnCompletion());
        assertEquals("val-completedBy", dto.getCompletedBy());
        assertTrue(dto.getArchived());
        assertEquals(99, dto.getRetryCount());
        assertEquals(99, dto.getMaxRetries());
        assertEquals("val-parentBatchId", dto.getParentBatchId());
        assertEquals("val-organizationId", dto.getOrganizationId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertTrue(dto.getValidationEnabled());
        assertTrue(dto.getStopOnError());
    }

    @Test
    void testEqualsAndHashCode() {
        IngestionBatchDto dto1 = IngestionBatchDto.builder()
                        .id("test-id")
            .batchId("test-batchId")
            .batchType(IngestionBatchDto.BatchTypeDto.FULL_IMPORT)
            .source("test-source")
            .sourceUrl("test-sourceUrl")
            .status(IngestionBatchDto.BatchStatusDto.PENDING)
            .startTime(LocalDateTime.of(2025,1,15,10,0))
            .endTime(LocalDateTime.of(2025,1,15,10,0))
            .totalRecords(42L)
            .processedRecords(42L)
            .successfulRecords(42L)
            .failedRecords(42L)
            .skippedRecords(42L)
            .progressPercentage(42)
            .schemaId("test-schemaId")
            .format("test-format")
            .fileSizeBytes(42L)
            .fileName("test-fileName")
            .fileChecksum("test-fileChecksum")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .createdDate(LocalDateTime.of(2025,1,15,10,0))
            .lastModifiedBy("test-lastModifiedBy")
            .lastModifiedDate(LocalDateTime.of(2025,1,15,10,0))
            .errorMessage("test-errorMessage")
            .errorMessages(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .statistics(Collections.emptyMap())
            .validationErrorCount(42L)
            .deleteOnCompletion(true)
            .completedBy("test-completedBy")
            .completedDate(LocalDateTime.of(2025,1,15,10,0))
            .archived(true)
            .archivedDate(LocalDateTime.of(2025,1,15,10,0))
            .retryCount(42)
            .maxRetries(42)
            .parentBatchId("test-parentBatchId")
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .validationEnabled(true)
            .stopOnError(true)
            .processingTimeMs(42L)
            .averageRecordProcessingTimeMs(null)
            .build();
        IngestionBatchDto dto2 = IngestionBatchDto.builder()
                        .id("test-id")
            .batchId("test-batchId")
            .batchType(IngestionBatchDto.BatchTypeDto.FULL_IMPORT)
            .source("test-source")
            .sourceUrl("test-sourceUrl")
            .status(IngestionBatchDto.BatchStatusDto.PENDING)
            .startTime(LocalDateTime.of(2025,1,15,10,0))
            .endTime(LocalDateTime.of(2025,1,15,10,0))
            .totalRecords(42L)
            .processedRecords(42L)
            .successfulRecords(42L)
            .failedRecords(42L)
            .skippedRecords(42L)
            .progressPercentage(42)
            .schemaId("test-schemaId")
            .format("test-format")
            .fileSizeBytes(42L)
            .fileName("test-fileName")
            .fileChecksum("test-fileChecksum")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .createdDate(LocalDateTime.of(2025,1,15,10,0))
            .lastModifiedBy("test-lastModifiedBy")
            .lastModifiedDate(LocalDateTime.of(2025,1,15,10,0))
            .errorMessage("test-errorMessage")
            .errorMessages(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .statistics(Collections.emptyMap())
            .validationErrorCount(42L)
            .deleteOnCompletion(true)
            .completedBy("test-completedBy")
            .completedDate(LocalDateTime.of(2025,1,15,10,0))
            .archived(true)
            .archivedDate(LocalDateTime.of(2025,1,15,10,0))
            .retryCount(42)
            .maxRetries(42)
            .parentBatchId("test-parentBatchId")
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .validationEnabled(true)
            .stopOnError(true)
            .processingTimeMs(42L)
            .averageRecordProcessingTimeMs(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        IngestionBatchDto dto = IngestionBatchDto.builder()
                        .id("test-id")
            .batchId("test-batchId")
            .batchType(IngestionBatchDto.BatchTypeDto.FULL_IMPORT)
            .source("test-source")
            .sourceUrl("test-sourceUrl")
            .status(IngestionBatchDto.BatchStatusDto.PENDING)
            .startTime(LocalDateTime.of(2025,1,15,10,0))
            .endTime(LocalDateTime.of(2025,1,15,10,0))
            .totalRecords(42L)
            .processedRecords(42L)
            .successfulRecords(42L)
            .failedRecords(42L)
            .skippedRecords(42L)
            .progressPercentage(42)
            .schemaId("test-schemaId")
            .format("test-format")
            .fileSizeBytes(42L)
            .fileName("test-fileName")
            .fileChecksum("test-fileChecksum")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .createdDate(LocalDateTime.of(2025,1,15,10,0))
            .lastModifiedBy("test-lastModifiedBy")
            .lastModifiedDate(LocalDateTime.of(2025,1,15,10,0))
            .errorMessage("test-errorMessage")
            .errorMessages(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .statistics(Collections.emptyMap())
            .validationErrorCount(42L)
            .deleteOnCompletion(true)
            .completedBy("test-completedBy")
            .completedDate(LocalDateTime.of(2025,1,15,10,0))
            .archived(true)
            .archivedDate(LocalDateTime.of(2025,1,15,10,0))
            .retryCount(42)
            .maxRetries(42)
            .parentBatchId("test-parentBatchId")
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .validationEnabled(true)
            .stopOnError(true)
            .processingTimeMs(42L)
            .averageRecordProcessingTimeMs(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}