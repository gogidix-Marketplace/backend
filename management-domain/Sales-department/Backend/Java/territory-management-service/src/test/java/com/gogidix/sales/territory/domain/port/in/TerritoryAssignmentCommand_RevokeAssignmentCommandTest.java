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
class TerritoryAssignmentCommand_RevokeAssignmentCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryAssignmentCommand.RevokeAssignmentCommand dto = new TerritoryAssignmentCommand.RevokeAssignmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setAssignmentId("val-assignmentId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-assignmentId", dto.getAssignmentId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentCommand.RevokeAssignmentCommand dto1 = new TerritoryAssignmentCommand.RevokeAssignmentCommand();
        TerritoryAssignmentCommand.RevokeAssignmentCommand dto2 = new TerritoryAssignmentCommand.RevokeAssignmentCommand();
        dto1.setTenantId("test");
        dto1.setAssignmentId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setAssignmentId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryAssignmentCommand.RevokeAssignmentCommand dto = new TerritoryAssignmentCommand.RevokeAssignmentCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryAssignmentCommand.RevokeAssignmentCommand dto = new TerritoryAssignmentCommand.RevokeAssignmentCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}