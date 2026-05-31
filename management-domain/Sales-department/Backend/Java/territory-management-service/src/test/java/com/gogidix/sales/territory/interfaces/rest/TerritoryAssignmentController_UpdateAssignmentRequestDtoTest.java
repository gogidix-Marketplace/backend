package com.gogidix.sales.territory.interfaces.rest;

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
class TerritoryAssignmentController_UpdateAssignmentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TerritoryAssignmentController.UpdateAssignmentRequestDto dto = new TerritoryAssignmentController.UpdateAssignmentRequestDto();
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPrimaryAssignment(true);
        dto.setPriority(99);
        dto.setNotes("val-notes");
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertTrue(dto.getPrimaryAssignment());
        assertEquals(99, dto.getPriority());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentController.UpdateAssignmentRequestDto dto1 = new TerritoryAssignmentController.UpdateAssignmentRequestDto();
        TerritoryAssignmentController.UpdateAssignmentRequestDto dto2 = new TerritoryAssignmentController.UpdateAssignmentRequestDto();
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setPrimaryAssignment(true);
        dto1.setPriority(42);
        dto1.setNotes("test");
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setPrimaryAssignment(true);
        dto2.setPriority(42);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setEndDate(LocalDate.of(2099,12,31));
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryAssignmentController.UpdateAssignmentRequestDto dto = new TerritoryAssignmentController.UpdateAssignmentRequestDto();
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
        TerritoryAssignmentController.UpdateAssignmentRequestDto dto = new TerritoryAssignmentController.UpdateAssignmentRequestDto();
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPrimaryAssignment(true);
        dto.setPriority(42);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}