package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.KPIBoardDto;
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
class KPIBoardDtoTest {

        @Test
    void testBuilder() {
        KPIBoardDto dto = KPIBoardDto.builder()
                        .id("test-id")
            .name("test-name")
            .description("test-description")
            .owner("test-owner")
            .viewers(Collections.emptyList())
            .scope("test-scope")
            .scopeId("test-scopeId")
            .kpis(Collections.emptyList())
            .layout(null)
            .preferences(null)
            .refreshSchedule(null)
            .status("test-status")
            .version(42)
            .templateId("test-templateId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-owner", dto.getOwner());
        assertEquals("test-scope", dto.getScope());
        assertEquals("test-scopeId", dto.getScopeId());
        assertEquals("test-status", dto.getStatus());
        assertEquals(42, dto.getVersion());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoardDto dto = new KPIBoardDto();
        dto.setId("val-id");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setOwner("val-owner");
        dto.setScope("val-scope");
        dto.setScopeId("val-scopeId");
        dto.setStatus("val-status");
        dto.setVersion(99);
        dto.setTemplateId("val-templateId");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-owner", dto.getOwner());
        assertEquals("val-scope", dto.getScope());
        assertEquals("val-scopeId", dto.getScopeId());
        assertEquals("val-status", dto.getStatus());
        assertEquals(99, dto.getVersion());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoardDto dto1 = KPIBoardDto.builder()
                        .id("test-id")
            .name("test-name")
            .description("test-description")
            .owner("test-owner")
            .viewers(Collections.emptyList())
            .scope("test-scope")
            .scopeId("test-scopeId")
            .kpis(Collections.emptyList())
            .layout(null)
            .preferences(null)
            .refreshSchedule(null)
            .status("test-status")
            .version(42)
            .templateId("test-templateId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedBy("test-updatedBy")
            .build();
        KPIBoardDto dto2 = KPIBoardDto.builder()
                        .id("test-id")
            .name("test-name")
            .description("test-description")
            .owner("test-owner")
            .viewers(Collections.emptyList())
            .scope("test-scope")
            .scopeId("test-scopeId")
            .kpis(Collections.emptyList())
            .layout(null)
            .preferences(null)
            .refreshSchedule(null)
            .status("test-status")
            .version(42)
            .templateId("test-templateId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoardDto dto = KPIBoardDto.builder()
                        .id("test-id")
            .name("test-name")
            .description("test-description")
            .owner("test-owner")
            .viewers(Collections.emptyList())
            .scope("test-scope")
            .scopeId("test-scopeId")
            .kpis(Collections.emptyList())
            .layout(null)
            .preferences(null)
            .refreshSchedule(null)
            .status("test-status")
            .version(42)
            .templateId("test-templateId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}