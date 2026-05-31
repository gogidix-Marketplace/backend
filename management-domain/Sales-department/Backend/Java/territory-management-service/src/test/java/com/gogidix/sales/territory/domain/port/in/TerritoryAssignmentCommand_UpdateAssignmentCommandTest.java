package com.gogidix.sales.territory.domain.port.in;

import com.gogidix.sales.territory.domain.port.in.TerritoryAssignmentCommand;
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
class TerritoryAssignmentCommand_UpdateAssignmentCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryAssignmentCommand.UpdateAssignmentCommand dto = new TerritoryAssignmentCommand.UpdateAssignmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setAssignmentId("val-assignmentId");
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPrimaryAssignment(true);
        dto.setPriority(99);
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-assignmentId", dto.getAssignmentId());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertTrue(dto.getPrimaryAssignment());
        assertEquals(99, dto.getPriority());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentCommand.UpdateAssignmentCommand dto1 = new TerritoryAssignmentCommand.UpdateAssignmentCommand();
        TerritoryAssignmentCommand.UpdateAssignmentCommand dto2 = new TerritoryAssignmentCommand.UpdateAssignmentCommand();
        dto1.setTenantId("test");
        dto1.setAssignmentId("test");
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setPrimaryAssignment(true);
        dto1.setPriority(42);
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setAssignmentId("test");
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setPrimaryAssignment(true);
        dto2.setPriority(42);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryAssignmentCommand.UpdateAssignmentCommand dto = new TerritoryAssignmentCommand.UpdateAssignmentCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
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
        TerritoryAssignmentCommand.UpdateAssignmentCommand dto = new TerritoryAssignmentCommand.UpdateAssignmentCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPrimaryAssignment(true);
        dto.setPriority(42);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}