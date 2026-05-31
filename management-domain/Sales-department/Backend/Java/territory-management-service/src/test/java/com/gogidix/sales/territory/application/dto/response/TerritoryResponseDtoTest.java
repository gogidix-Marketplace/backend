package com.gogidix.sales.territory.application.dto.response;

import com.gogidix.sales.territory.application.dto.response.TerritoryResponseDto;
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
class TerritoryResponseDtoTest {

        @Test
    void testBuilder() {
        TerritoryResponseDto dto = TerritoryResponseDto.builder()
                        .id("test-id")
            .territoryId("test-territoryId")
            .tenantId("test-tenantId")
            .name("test-name")
            .code("test-code")
            .description("test-description")
            .type(TerritoryResponseDto.TerritoryTypeDto.GEOGRAPHIC)
            .status(TerritoryResponseDto.TerritoryStatusDto.ACTIVE)
            .geographicBoundary(null)
            .productCategories(Collections.emptyList())
            .productIds(Collections.emptyList())
            .customerSegments(Collections.emptyList())
            .customerTierIds(Collections.emptyList())
            .parentTerritoryId("test-parentTerritoryId")
            .childTerritoryIds(Collections.emptyList())
            .regionId("test-regionId")
            .managerId("test-managerId")
            .priority(42)
            .performance(null)
            .pendingRealignment(true)
            .realignmentRequestedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .realignmentRequestedBy("test-realignmentRequestedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-territoryId", dto.getTerritoryId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-code", dto.getCode());
        assertEquals("test-description", dto.getDescription());
        assertEquals(TerritoryResponseDto.TerritoryTypeDto.GEOGRAPHIC, dto.getType());
        assertEquals(TerritoryResponseDto.TerritoryStatusDto.ACTIVE, dto.getStatus());
        assertEquals("test-parentTerritoryId", dto.getParentTerritoryId());
        assertEquals("test-regionId", dto.getRegionId());
        assertEquals("test-managerId", dto.getManagerId());
        assertEquals(42, dto.getPriority());
        assertTrue(dto.getPendingRealignment());
        assertEquals("test-realignmentRequestedBy", dto.getRealignmentRequestedBy());
    }

    @Test
    void testSettersAndGetters() {
        TerritoryResponseDto dto = new TerritoryResponseDto();
        dto.setId("val-id");
        dto.setTerritoryId("val-territoryId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setCode("val-code");
        dto.setDescription("val-description");
        dto.setType(TerritoryResponseDto.TerritoryTypeDto.GEOGRAPHIC);
        dto.setStatus(TerritoryResponseDto.TerritoryStatusDto.ACTIVE);
        dto.setParentTerritoryId("val-parentTerritoryId");
        dto.setRegionId("val-regionId");
        dto.setManagerId("val-managerId");
        dto.setPriority(99);
        dto.setPendingRealignment(true);
        dto.setRealignmentRequestedBy("val-realignmentRequestedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-code", dto.getCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals(TerritoryResponseDto.TerritoryTypeDto.GEOGRAPHIC, dto.getType());
        assertEquals(TerritoryResponseDto.TerritoryStatusDto.ACTIVE, dto.getStatus());
        assertEquals("val-parentTerritoryId", dto.getParentTerritoryId());
        assertEquals("val-regionId", dto.getRegionId());
        assertEquals("val-managerId", dto.getManagerId());
        assertEquals(99, dto.getPriority());
        assertTrue(dto.getPendingRealignment());
        assertEquals("val-realignmentRequestedBy", dto.getRealignmentRequestedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryResponseDto dto1 = TerritoryResponseDto.builder()
                        .id("test-id")
            .territoryId("test-territoryId")
            .tenantId("test-tenantId")
            .name("test-name")
            .code("test-code")
            .description("test-description")
            .type(TerritoryResponseDto.TerritoryTypeDto.GEOGRAPHIC)
            .status(TerritoryResponseDto.TerritoryStatusDto.ACTIVE)
            .geographicBoundary(null)
            .productCategories(Collections.emptyList())
            .productIds(Collections.emptyList())
            .customerSegments(Collections.emptyList())
            .customerTierIds(Collections.emptyList())
            .parentTerritoryId("test-parentTerritoryId")
            .childTerritoryIds(Collections.emptyList())
            .regionId("test-regionId")
            .managerId("test-managerId")
            .priority(42)
            .performance(null)
            .pendingRealignment(true)
            .realignmentRequestedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .realignmentRequestedBy("test-realignmentRequestedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TerritoryResponseDto dto2 = TerritoryResponseDto.builder()
                        .id("test-id")
            .territoryId("test-territoryId")
            .tenantId("test-tenantId")
            .name("test-name")
            .code("test-code")
            .description("test-description")
            .type(TerritoryResponseDto.TerritoryTypeDto.GEOGRAPHIC)
            .status(TerritoryResponseDto.TerritoryStatusDto.ACTIVE)
            .geographicBoundary(null)
            .productCategories(Collections.emptyList())
            .productIds(Collections.emptyList())
            .customerSegments(Collections.emptyList())
            .customerTierIds(Collections.emptyList())
            .parentTerritoryId("test-parentTerritoryId")
            .childTerritoryIds(Collections.emptyList())
            .regionId("test-regionId")
            .managerId("test-managerId")
            .priority(42)
            .performance(null)
            .pendingRealignment(true)
            .realignmentRequestedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .realignmentRequestedBy("test-realignmentRequestedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TerritoryResponseDto dto = TerritoryResponseDto.builder()
                        .id("test-id")
            .territoryId("test-territoryId")
            .tenantId("test-tenantId")
            .name("test-name")
            .code("test-code")
            .description("test-description")
            .type(TerritoryResponseDto.TerritoryTypeDto.GEOGRAPHIC)
            .status(TerritoryResponseDto.TerritoryStatusDto.ACTIVE)
            .geographicBoundary(null)
            .productCategories(Collections.emptyList())
            .productIds(Collections.emptyList())
            .customerSegments(Collections.emptyList())
            .customerTierIds(Collections.emptyList())
            .parentTerritoryId("test-parentTerritoryId")
            .childTerritoryIds(Collections.emptyList())
            .regionId("test-regionId")
            .managerId("test-managerId")
            .priority(42)
            .performance(null)
            .pendingRealignment(true)
            .realignmentRequestedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .realignmentRequestedBy("test-realignmentRequestedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}