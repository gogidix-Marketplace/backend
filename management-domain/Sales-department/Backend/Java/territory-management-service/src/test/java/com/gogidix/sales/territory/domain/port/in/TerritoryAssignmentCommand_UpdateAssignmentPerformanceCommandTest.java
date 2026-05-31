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
class TerritoryAssignmentCommand_UpdateAssignmentPerformanceCommandTest {

        @Test
    void testSettersAndGetters() {
        TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand dto = new TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand();
        dto.setTenantId("val-tenantId");
        dto.setAssignmentId("val-assignmentId");
        dto.setAccountsManaged(99);
        dto.setDealsClosed(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-assignmentId", dto.getAssignmentId());
        assertEquals(99, dto.getAccountsManaged());
        assertEquals(99, dto.getDealsClosed());
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand dto1 = new TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand();
        TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand dto2 = new TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand();
        dto1.setTenantId("test");
        dto1.setAssignmentId("test");
        dto1.setSalesGenerated(null);
        dto1.setAccountsManaged(42);
        dto1.setDealsClosed(42);
        dto2.setTenantId("test");
        dto2.setAssignmentId("test");
        dto2.setSalesGenerated(null);
        dto2.setAccountsManaged(42);
        dto2.setDealsClosed(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand dto = new TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
        dto.setSalesGenerated(null);
        dto.setAccountsManaged(42);
        dto.setDealsClosed(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand dto = new TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand();
        dto.setTenantId("test");
        dto.setAssignmentId("test");
        dto.setSalesGenerated(null);
        dto.setAccountsManaged(42);
        dto.setDealsClosed(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}