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
class RevenueCommand_DeleteRevenueCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.DeleteRevenueCommand dto = new RevenueCommand.DeleteRevenueCommand();
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.DeleteRevenueCommand dto1 = new RevenueCommand.DeleteRevenueCommand();
        RevenueCommand.DeleteRevenueCommand dto2 = new RevenueCommand.DeleteRevenueCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.DeleteRevenueCommand dto = new RevenueCommand.DeleteRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.DeleteRevenueCommand dto = new RevenueCommand.DeleteRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}