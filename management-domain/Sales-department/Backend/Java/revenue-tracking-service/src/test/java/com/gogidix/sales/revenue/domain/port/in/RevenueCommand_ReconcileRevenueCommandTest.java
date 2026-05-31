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
class RevenueCommand_ReconcileRevenueCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.ReconcileRevenueCommand dto = new RevenueCommand.ReconcileRevenueCommand();
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        dto.setReconciliationId("val-reconciliationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.ReconcileRevenueCommand dto1 = new RevenueCommand.ReconcileRevenueCommand();
        RevenueCommand.ReconcileRevenueCommand dto2 = new RevenueCommand.ReconcileRevenueCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto1.setReconciliationId("test");
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        dto2.setReconciliationId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.ReconcileRevenueCommand dto = new RevenueCommand.ReconcileRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setReconciliationId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.ReconcileRevenueCommand dto = new RevenueCommand.ReconcileRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setReconciliationId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}