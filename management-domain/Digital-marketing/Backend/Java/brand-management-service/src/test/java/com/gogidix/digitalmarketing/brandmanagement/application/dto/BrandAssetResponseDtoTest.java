package com.gogidix.digitalmarketing.brandmanagement.application.dto;

import com.gogidix.digitalmarketing.brandmanagement.application.dto.BrandAssetResponseDto;
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
class BrandAssetResponseDtoTest {

        @Test
    void testBuilder() {
        BrandAssetResponseDto dto = BrandAssetResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .url("test-url")
            .category("test-category")
            .description("test-description")
            .status("test-status")
            .version("test-version")
            .fileFormat("test-fileFormat")
            .fileSize("test-fileSize")
            .storageLocation("test-storageLocation")
            .isActive("test-isActive")
            .isPublic("test-isPublic")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-type", dto.getType());
        assertEquals("test-url", dto.getUrl());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-version", dto.getVersion());
        assertEquals("test-fileFormat", dto.getFileFormat());
        assertEquals("test-fileSize", dto.getFileSize());
        assertEquals("test-storageLocation", dto.getStorageLocation());
        assertEquals("test-isActive", dto.getIsActive());
        assertEquals("test-isPublic", dto.getIsPublic());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        BrandAssetResponseDto dto = new BrandAssetResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setType("val-type");
        dto.setUrl("val-url");
        dto.setCategory("val-category");
        dto.setDescription("val-description");
        dto.setStatus("val-status");
        dto.setVersion("val-version");
        dto.setFileFormat("val-fileFormat");
        dto.setFileSize("val-fileSize");
        dto.setStorageLocation("val-storageLocation");
        dto.setIsActive("val-isActive");
        dto.setIsPublic("val-isPublic");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-type", dto.getType());
        assertEquals("val-url", dto.getUrl());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-version", dto.getVersion());
        assertEquals("val-fileFormat", dto.getFileFormat());
        assertEquals("val-fileSize", dto.getFileSize());
        assertEquals("val-storageLocation", dto.getStorageLocation());
        assertEquals("val-isActive", dto.getIsActive());
        assertEquals("val-isPublic", dto.getIsPublic());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BrandAssetResponseDto dto1 = BrandAssetResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .url("test-url")
            .category("test-category")
            .description("test-description")
            .status("test-status")
            .version("test-version")
            .fileFormat("test-fileFormat")
            .fileSize("test-fileSize")
            .storageLocation("test-storageLocation")
            .isActive("test-isActive")
            .isPublic("test-isPublic")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BrandAssetResponseDto dto2 = BrandAssetResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .url("test-url")
            .category("test-category")
            .description("test-description")
            .status("test-status")
            .version("test-version")
            .fileFormat("test-fileFormat")
            .fileSize("test-fileSize")
            .storageLocation("test-storageLocation")
            .isActive("test-isActive")
            .isPublic("test-isPublic")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BrandAssetResponseDto dto = BrandAssetResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .url("test-url")
            .category("test-category")
            .description("test-description")
            .status("test-status")
            .version("test-version")
            .fileFormat("test-fileFormat")
            .fileSize("test-fileSize")
            .storageLocation("test-storageLocation")
            .isActive("test-isActive")
            .isPublic("test-isPublic")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}