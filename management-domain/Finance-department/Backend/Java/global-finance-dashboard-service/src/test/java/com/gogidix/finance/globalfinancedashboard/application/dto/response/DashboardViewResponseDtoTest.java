package com.gogidix.finance.globalfinancedashboard.application.dto.response;

import com.gogidix.finance.globalfinancedashboard.application.dto.response.DashboardViewResponseDto;
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
class DashboardViewResponseDtoTest {

        @Test
    void testBuilder() {
        DashboardViewResponseDto dto = DashboardViewResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .widgets(Collections.emptyList())
            .filters(Collections.emptyMap())
            .ownerId("test-ownerId")
            .isPublic(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertTrue(dto.isPublic());
    }

    @Test
    void testSettersAndGetters() {
        DashboardViewResponseDto dto = new DashboardViewResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setOwnerId("val-ownerId");
        dto.setPublic(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertTrue(dto.isPublic());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardViewResponseDto dto1 = DashboardViewResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .widgets(Collections.emptyList())
            .filters(Collections.emptyMap())
            .ownerId("test-ownerId")
            .isPublic(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        DashboardViewResponseDto dto2 = DashboardViewResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .widgets(Collections.emptyList())
            .filters(Collections.emptyMap())
            .ownerId("test-ownerId")
            .isPublic(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardViewResponseDto dto = DashboardViewResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .widgets(Collections.emptyList())
            .filters(Collections.emptyMap())
            .ownerId("test-ownerId")
            .isPublic(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}