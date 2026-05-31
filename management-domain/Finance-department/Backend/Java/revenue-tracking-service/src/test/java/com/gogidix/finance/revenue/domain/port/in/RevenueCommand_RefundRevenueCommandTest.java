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
class RevenueCommand_RefundRevenueCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.RefundRevenueCommand dto = new RevenueCommand.RefundRevenueCommand();
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        dto.setRefundAmount(BigDecimal.ONE);
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals(BigDecimal.ONE, dto.getRefundAmount());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.RefundRevenueCommand dto1 = new RevenueCommand.RefundRevenueCommand();
        RevenueCommand.RefundRevenueCommand dto2 = new RevenueCommand.RefundRevenueCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto1.setRefundAmount(BigDecimal.TEN);
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        dto2.setRefundAmount(BigDecimal.TEN);
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.RefundRevenueCommand dto = new RevenueCommand.RefundRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setRefundAmount(BigDecimal.TEN);
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.RefundRevenueCommand dto = new RevenueCommand.RefundRevenueCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setRefundAmount(BigDecimal.TEN);
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}