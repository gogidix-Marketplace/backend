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
class RevenueCommand_DeferRevenueCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.DeferRevenueCommand dto = new RevenueCommand.DeferRevenueCommand();
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        dto.setPeriods(99);
        dto.setStartDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals(99, dto.getPeriods());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.DeferRevenueCommand dto1 = new RevenueCommand.DeferRevenueCommand();
        RevenueCommand.DeferRevenueCommand dto2 = new RevenueCommand.DeferRevenueCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto1.setPeriods(42);
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        dto2.setPeriods(42);
        dto2.setStartDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.DeferRevenueCommand dto = new RevenueCommand.DeferRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setPeriods(42);
        dto.setStartDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.DeferRevenueCommand dto = new RevenueCommand.DeferRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setPeriods(42);
        dto.setStartDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}