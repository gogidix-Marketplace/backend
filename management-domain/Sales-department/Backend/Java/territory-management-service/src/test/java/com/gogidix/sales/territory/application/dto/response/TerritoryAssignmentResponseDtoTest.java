package com.gogidix.sales.territory.application.dto.response;

import com.gogidix.sales.territory.application.dto.response.TerritoryAssignmentResponseDto;
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
class TerritoryAssignmentResponseDtoTest {

        @Test
    void testBuilder() {
        TerritoryAssignmentResponseDto dto = TerritoryAssignmentResponseDto.builder()
                        .id("test-id")
            .assignmentId("test-assignmentId")
            .tenantId("test-tenantId")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .salesRepresentativeId("test-salesRepresentativeId")
            .salesRepresentativeName("test-salesRepresentativeName")
            .status(TerritoryAssignmentResponseDto.AssignmentStatusDto.ACTIVE)
            .type(TerritoryAssignmentResponseDto.AssignmentTypeDto.FULL_TIME)
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedBy("test-assignedBy")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .primaryAssignment(true)
            .priority(42)
            .notes("test-notes")
            .performance(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-assignmentId", dto.getAssignmentId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-territoryId", dto.getTerritoryId());
        assertEquals("test-territoryName", dto.getTerritoryName());
        assertEquals("test-salesRepresentativeId", dto.getSalesRepresentativeId());
        assertEquals("test-salesRepresentativeName", dto.getSalesRepresentativeName());
        assertEquals(TerritoryAssignmentResponseDto.AssignmentStatusDto.ACTIVE, dto.getStatus());
        assertEquals(TerritoryAssignmentResponseDto.AssignmentTypeDto.FULL_TIME, dto.getType());
        assertEquals("test-assignedBy", dto.getAssignedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertTrue(dto.getPrimaryAssignment());
        assertEquals(42, dto.getPriority());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        TerritoryAssignmentResponseDto dto = new TerritoryAssignmentResponseDto();
        dto.setId("val-id");
        dto.setAssignmentId("val-assignmentId");
        dto.setTenantId("val-tenantId");
        dto.setTerritoryId("val-territoryId");
        dto.setTerritoryName("val-territoryName");
        dto.setSalesRepresentativeId("val-salesRepresentativeId");
        dto.setSalesRepresentativeName("val-salesRepresentativeName");
        dto.setStatus(TerritoryAssignmentResponseDto.AssignmentStatusDto.ACTIVE);
        dto.setType(TerritoryAssignmentResponseDto.AssignmentTypeDto.FULL_TIME);
        dto.setAssignedBy("val-assignedBy");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPrimaryAssignment(true);
        dto.setPriority(99);
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-assignmentId", dto.getAssignmentId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-territoryName", dto.getTerritoryName());
        assertEquals("val-salesRepresentativeId", dto.getSalesRepresentativeId());
        assertEquals("val-salesRepresentativeName", dto.getSalesRepresentativeName());
        assertEquals(TerritoryAssignmentResponseDto.AssignmentStatusDto.ACTIVE, dto.getStatus());
        assertEquals(TerritoryAssignmentResponseDto.AssignmentTypeDto.FULL_TIME, dto.getType());
        assertEquals("val-assignedBy", dto.getAssignedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertTrue(dto.getPrimaryAssignment());
        assertEquals(99, dto.getPriority());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentResponseDto dto1 = TerritoryAssignmentResponseDto.builder()
                        .id("test-id")
            .assignmentId("test-assignmentId")
            .tenantId("test-tenantId")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .salesRepresentativeId("test-salesRepresentativeId")
            .salesRepresentativeName("test-salesRepresentativeName")
            .status(TerritoryAssignmentResponseDto.AssignmentStatusDto.ACTIVE)
            .type(TerritoryAssignmentResponseDto.AssignmentTypeDto.FULL_TIME)
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedBy("test-assignedBy")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .primaryAssignment(true)
            .priority(42)
            .notes("test-notes")
            .performance(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TerritoryAssignmentResponseDto dto2 = TerritoryAssignmentResponseDto.builder()
                        .id("test-id")
            .assignmentId("test-assignmentId")
            .tenantId("test-tenantId")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .salesRepresentativeId("test-salesRepresentativeId")
            .salesRepresentativeName("test-salesRepresentativeName")
            .status(TerritoryAssignmentResponseDto.AssignmentStatusDto.ACTIVE)
            .type(TerritoryAssignmentResponseDto.AssignmentTypeDto.FULL_TIME)
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedBy("test-assignedBy")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .primaryAssignment(true)
            .priority(42)
            .notes("test-notes")
            .performance(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TerritoryAssignmentResponseDto dto = TerritoryAssignmentResponseDto.builder()
                        .id("test-id")
            .assignmentId("test-assignmentId")
            .tenantId("test-tenantId")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .salesRepresentativeId("test-salesRepresentativeId")
            .salesRepresentativeName("test-salesRepresentativeName")
            .status(TerritoryAssignmentResponseDto.AssignmentStatusDto.ACTIVE)
            .type(TerritoryAssignmentResponseDto.AssignmentTypeDto.FULL_TIME)
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedBy("test-assignedBy")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .primaryAssignment(true)
            .priority(42)
            .notes("test-notes")
            .performance(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}