package com.gogidix.sales.territory.interfaces.rest;

import com.gogidix.sales.territory.domain.model.TerritoryAssignment;
import com.gogidix.sales.territory.interfaces.rest.TerritoryAssignmentController;
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
class TerritoryAssignmentController_CreateAssignmentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TerritoryAssignmentController.CreateAssignmentRequestDto dto = new TerritoryAssignmentController.CreateAssignmentRequestDto();
        dto.setTerritoryId("val-territoryId");
        dto.setSalesRepresentativeId("val-salesRepresentativeId");
        dto.setSalesRepresentativeName("val-salesRepresentativeName");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPrimaryAssignment(true);
        dto.setPriority(99);
        dto.setNotes("val-notes");
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-salesRepresentativeId", dto.getSalesRepresentativeId());
        assertEquals("val-salesRepresentativeName", dto.getSalesRepresentativeName());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertTrue(dto.getPrimaryAssignment());
        assertEquals(99, dto.getPriority());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentController.CreateAssignmentRequestDto dto1 = new TerritoryAssignmentController.CreateAssignmentRequestDto();
        TerritoryAssignmentController.CreateAssignmentRequestDto dto2 = new TerritoryAssignmentController.CreateAssignmentRequestDto();
        dto1.setTerritoryId("test");
        dto1.setSalesRepresentativeId("test");
        dto1.setSalesRepresentativeName("test");
        dto1.setType(TerritoryAssignment.AssignmentType.FULL_TIME);
        dto1.setEffectiveDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setPrimaryAssignment(true);
        dto1.setPriority(42);
        dto1.setNotes("test");
        dto2.setTerritoryId("test");
        dto2.setSalesRepresentativeId("test");
        dto2.setSalesRepresentativeName("test");
        dto2.setType(TerritoryAssignment.AssignmentType.FULL_TIME);
        dto2.setEffectiveDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setPrimaryAssignment(true);
        dto2.setPriority(42);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTerritoryId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryAssignmentController.CreateAssignmentRequestDto dto = new TerritoryAssignmentController.CreateAssignmentRequestDto();
        dto.setTerritoryId("test");
        dto.setSalesRepresentativeId("test");
        dto.setSalesRepresentativeName("test");
        dto.setType(TerritoryAssignment.AssignmentType.FULL_TIME);
        dto.setEffectiveDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPrimaryAssignment(true);
        dto.setPriority(42);
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryAssignmentController.CreateAssignmentRequestDto dto = new TerritoryAssignmentController.CreateAssignmentRequestDto();
        dto.setTerritoryId("test");
        dto.setSalesRepresentativeId("test");
        dto.setSalesRepresentativeName("test");
        dto.setType(TerritoryAssignment.AssignmentType.FULL_TIME);
        dto.setEffectiveDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPrimaryAssignment(true);
        dto.setPriority(42);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}