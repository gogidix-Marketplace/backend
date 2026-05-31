package com.gogidix.management.executive.audit.application.dto;

import com.gogidix.management.executive.audit.application.dto.AuditDto;
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
class AuditDtoTest {

        @Test
    void testBuilder() {
        AuditDto dto = AuditDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ownerId("test-ownerId")
            .widgets(Collections.emptyList())
            .layout("test-layout")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertEquals("test-layout", dto.getLayout());
    }

    @Test
    void testSettersAndGetters() {
        AuditDto dto = new AuditDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setOwnerId("val-ownerId");
        dto.setLayout("val-layout");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-layout", dto.getLayout());
    }

    @Test
    void testEqualsAndHashCode() {
        AuditDto dto1 = AuditDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ownerId("test-ownerId")
            .widgets(Collections.emptyList())
            .layout("test-layout")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        AuditDto dto2 = AuditDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ownerId("test-ownerId")
            .widgets(Collections.emptyList())
            .layout("test-layout")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AuditDto dto = AuditDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ownerId("test-ownerId")
            .widgets(Collections.emptyList())
            .layout("test-layout")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}