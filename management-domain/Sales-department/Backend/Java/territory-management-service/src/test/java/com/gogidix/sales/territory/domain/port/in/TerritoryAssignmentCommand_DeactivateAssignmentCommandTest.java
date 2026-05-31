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
class TerritoryAssignmentCommand_DeactivateAssignmentCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryAssignmentCommand.DeactivateAssignmentCommand dto = new TerritoryAssignmentCommand.DeactivateAssignmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setAssignmentId("val-assignmentId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-assignmentId", dto.getAssignmentId());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentCommand.DeactivateAssignmentCommand dto1 = new TerritoryAssignmentCommand.DeactivateAssignmentCommand();
        TerritoryAssignmentCommand.DeactivateAssignmentCommand dto2 = new TerritoryAssignmentCommand.DeactivateAssignmentCommand();
        dto1.setTenantId("test");
        dto1.setAssignmentId("test");
        dto2.setTenantId("test");
        dto2.setAssignmentId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryAssignmentCommand.DeactivateAssignmentCommand dto = new TerritoryAssignmentCommand.DeactivateAssignmentCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryAssignmentCommand.DeactivateAssignmentCommand dto = new TerritoryAssignmentCommand.DeactivateAssignmentCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}