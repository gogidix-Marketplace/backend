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
class TerritoryAssignmentCommand_ActivateAssignmentCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryAssignmentCommand.ActivateAssignmentCommand dto = new TerritoryAssignmentCommand.ActivateAssignmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setAssignmentId("val-assignmentId");
        dto.setActivatedBy("val-activatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-assignmentId", dto.getAssignmentId());
        assertEquals("val-activatedBy", dto.getActivatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentCommand.ActivateAssignmentCommand dto1 = new TerritoryAssignmentCommand.ActivateAssignmentCommand();
        TerritoryAssignmentCommand.ActivateAssignmentCommand dto2 = new TerritoryAssignmentCommand.ActivateAssignmentCommand();
        dto1.setTenantId("test");
        dto1.setAssignmentId("test");
        dto1.setActivatedBy("test");
        dto2.setTenantId("test");
        dto2.setAssignmentId("test");
        dto2.setActivatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryAssignmentCommand.ActivateAssignmentCommand dto = new TerritoryAssignmentCommand.ActivateAssignmentCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
        dto.setActivatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryAssignmentCommand.ActivateAssignmentCommand dto = new TerritoryAssignmentCommand.ActivateAssignmentCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
        dto.setActivatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}