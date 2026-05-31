package com.gogidix.globalbusinessmanagement.export.application.dto;

import com.gogidix.globalbusinessmanagement.export.application.dto.ExportJobResponseDto;
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
class ExportJobResponseDtoTest {

        @Test
    void testBuilder() {
        ExportJobResponseDto dto = ExportJobResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .exportType("test-exportType")
            .format("test-format")
            .status("test-status")
            .filePath("test-filePath")
            .recordCount("test-recordCount")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-exportType", dto.getExportType());
        assertEquals("test-format", dto.getFormat());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-filePath", dto.getFilePath());
        assertEquals("test-recordCount", dto.getRecordCount());
    }

    @Test
    void testSettersAndGetters() {
        ExportJobResponseDto dto = new ExportJobResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setExportType("val-exportType");
        dto.setFormat("val-format");
        dto.setStatus("val-status");
        dto.setFilePath("val-filePath");
        dto.setRecordCount("val-recordCount");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-exportType", dto.getExportType());
        assertEquals("val-format", dto.getFormat());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-filePath", dto.getFilePath());
        assertEquals("val-recordCount", dto.getRecordCount());
    }

    @Test
    void testEqualsAndHashCode() {
        ExportJobResponseDto dto1 = ExportJobResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .exportType("test-exportType")
            .format("test-format")
            .status("test-status")
            .filePath("test-filePath")
            .recordCount("test-recordCount")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ExportJobResponseDto dto2 = ExportJobResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .exportType("test-exportType")
            .format("test-format")
            .status("test-status")
            .filePath("test-filePath")
            .recordCount("test-recordCount")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ExportJobResponseDto dto = ExportJobResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .exportType("test-exportType")
            .format("test-format")
            .status("test-status")
            .filePath("test-filePath")
            .recordCount("test-recordCount")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}