package com.gogidix.sales.revenue.domain.port.in;

import com.gogidix.sales.revenue.domain.port.in.RevenueCommand;
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
class RevenueCommand_UpdateRevenueCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.UpdateRevenueCommand dto = new RevenueCommand.UpdateRevenueCommand();
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setSalespersonId("val-salespersonId");
        dto.setSalespersonName("val-salespersonName");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-salespersonId", dto.getSalespersonId());
        assertEquals("val-salespersonName", dto.getSalespersonName());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.UpdateRevenueCommand dto1 = new RevenueCommand.UpdateRevenueCommand();
        RevenueCommand.UpdateRevenueCommand dto2 = new RevenueCommand.UpdateRevenueCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto1.setDescription("test");
        dto1.setNotes("test");
        dto1.setTags(Collections.emptyList());
        dto1.setSalespersonId("test");
        dto1.setSalespersonName("test");
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        dto2.setDescription("test");
        dto2.setNotes("test");
        dto2.setTags(Collections.emptyList());
        dto2.setSalespersonId("test");
        dto2.setSalespersonName("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.UpdateRevenueCommand dto = new RevenueCommand.UpdateRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setDescription("test");
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        dto.setSalespersonId("test");
        dto.setSalespersonName("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.UpdateRevenueCommand dto = new RevenueCommand.UpdateRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setDescription("test");
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        dto.setSalespersonId("test");
        dto.setSalespersonName("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}