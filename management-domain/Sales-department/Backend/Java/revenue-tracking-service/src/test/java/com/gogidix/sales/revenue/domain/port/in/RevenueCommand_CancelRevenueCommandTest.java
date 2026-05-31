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
class RevenueCommand_CancelRevenueCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.CancelRevenueCommand dto = new RevenueCommand.CancelRevenueCommand();
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.CancelRevenueCommand dto1 = new RevenueCommand.CancelRevenueCommand();
        RevenueCommand.CancelRevenueCommand dto2 = new RevenueCommand.CancelRevenueCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.CancelRevenueCommand dto = new RevenueCommand.CancelRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.CancelRevenueCommand dto = new RevenueCommand.CancelRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}