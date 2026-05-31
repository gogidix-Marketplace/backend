package com.gogidix.globalbusinessmanagement.localization.application.dto;

import com.gogidix.globalbusinessmanagement.localization.application.dto.LocalizationEntryRequestDto;
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
class LocalizationEntryRequestDtoTest {

        @Test
    void testBuilder() {
        LocalizationEntryRequestDto dto = LocalizationEntryRequestDto.builder()
                        .tenantId("test-tenantId")
            .key("test-key")
            .value("test-value")
            .language("test-language")
            .region("test-region")
            .module("test-module")
            .isActive("test-isActive")
            .build();
        assertNotNull(dto);
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
        LocalizationEntryRequestDto dto = new LocalizationEntryRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setKey("val-key");
        dto.setValue("val-value");
        dto.setLanguage("val-language");
        dto.setRegion("val-region");
        dto.setModule("val-module");
        dto.setIsActive("val-isActive");
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
        LocalizationEntryRequestDto dto1 = LocalizationEntryRequestDto.builder()
                        .tenantId("test-tenantId")
            .key("test-key")
            .value("test-value")
            .language("test-language")
            .region("test-region")
            .module("test-module")
            .isActive("test-isActive")
            .build();
        LocalizationEntryRequestDto dto2 = LocalizationEntryRequestDto.builder()
                        .tenantId("test-tenantId")
            .key("test-key")
            .value("test-value")
            .language("test-language")
            .region("test-region")
            .module("test-module")
            .isActive("test-isActive")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LocalizationEntryRequestDto dto = LocalizationEntryRequestDto.builder()
                        .tenantId("test-tenantId")
            .key("test-key")
            .value("test-value")
            .language("test-language")
            .region("test-region")
            .module("test-module")
            .isActive("test-isActive")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}