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
class RevenueCommand_CompleteMilestoneCommandTest {

        @Test
    void testSettersAndGetters() {
        RevenueCommand.CompleteMilestoneCommand dto = new RevenueCommand.CompleteMilestoneCommand();
        dto.setTenantId("val-tenantId");
        dto.setRevenueId("val-revenueId");
        dto.setMilestoneId("val-milestoneId");
        dto.setCompletedDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals("val-milestoneId", dto.getMilestoneId());
        assertEquals(LocalDate.of(2025,6,1), dto.getCompletedDate());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueCommand.CompleteMilestoneCommand dto1 = new RevenueCommand.CompleteMilestoneCommand();
        RevenueCommand.CompleteMilestoneCommand dto2 = new RevenueCommand.CompleteMilestoneCommand();
        dto1.setTenantId("test");
        dto1.setRevenueId("test");
        dto1.setMilestoneId("test");
        dto1.setCompletedDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setRevenueId("test");
        dto2.setMilestoneId("test");
        dto2.setCompletedDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RevenueCommand.CompleteMilestoneCommand dto = new RevenueCommand.CompleteMilestoneCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setMilestoneId("test");
        dto.setCompletedDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RevenueCommand.CompleteMilestoneCommand dto = new RevenueCommand.CompleteMilestoneCommand();
        dto.setTenantId("test");
        dto.setRevenueId("test");
        dto.setMilestoneId("test");
        dto.setCompletedDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}