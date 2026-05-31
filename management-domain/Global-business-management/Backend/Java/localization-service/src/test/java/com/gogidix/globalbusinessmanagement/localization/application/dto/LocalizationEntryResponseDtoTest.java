package com.gogidix.globalbusinessmanagement.localization.application.dto;

import com.gogidix.globalbusinessmanagement.localization.application.dto.LocalizationEntryResponseDto;
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
class LocalizationEntryResponseDtoTest {

        @Test
    void testBuilder() {
        LocalizationEntryResponseDto dto = LocalizationEntryResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .key("test-key")
            .value("test-value")
            .language("test-language")
            .region("test-region")
            .module("test-module")
            .isActive("test-isActive")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-key", dto.getKey());
        assertEquals("test-value", dto.getValue());
        assertEquals("test-language", dto.getLanguage());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-module", dto.getModule());
        assertEquals("test-isActive", dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        LocalizationEntryResponseDto dto = new LocalizationEntryResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setKey("val-key");
        dto.setValue("val-value");
        dto.setLanguage("val-language");
        dto.setRegion("val-region");
        dto.setModule("val-module");
        dto.setIsActive("val-isActive");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-key", dto.getKey());
        assertEquals("val-value", dto.getValue());
        assertEquals("val-language", dto.getLanguage());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-module", dto.getModule());
        assertEquals("val-isActive", dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalizationEntryResponseDto dto1 = LocalizationEntryResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .key("test-key")
            .value("test-value")
            .language("test-language")
            .region("test-region")
            .module("test-module")
            .isActive("test-isActive")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        LocalizationEntryResponseDto dto2 = LocalizationEntryResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .key("test-key")
            .value("test-value")
            .language("test-language")
            .region("test-region")
            .module("test-module")
            .isActive("test-isActive")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LocalizationEntryResponseDto dto = LocalizationEntryResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .key("test-key")
            .value("test-value")
            .language("test-language")
            .region("test-region")
            .module("test-module")
            .isActive("test-isActive")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}