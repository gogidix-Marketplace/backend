package com.gogidix.finance.revenue.domain.port.in;

import com.gogidix.finance.revenue.domain.port.in.RevenueCommand;
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
class RevenueCommand_MarkAsPaidCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.MarkAsPaidCommand dto = new RevenueCommand.MarkAsPaidCommand();
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        dto.setPaidDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaidDate());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.MarkAsPaidCommand dto1 = new RevenueCommand.MarkAsPaidCommand();
        RevenueCommand.MarkAsPaidCommand dto2 = new RevenueCommand.MarkAsPaidCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto1.setPaidDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        dto2.setPaidDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.MarkAsPaidCommand dto = new RevenueCommand.MarkAsPaidCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setPaidDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.MarkAsPaidCommand dto = new RevenueCommand.MarkAsPaidCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setPaidDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}