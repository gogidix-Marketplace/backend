package com.gogidix.finance.revenue.domain.model;

import com.gogidix.finance.revenue.domain.model.Revenue;
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
class Revenue_RevenueMilestoneTest {

        @Test
    void testSettersAndGetters() {
        Revenue.RevenueMilestone dto = new Revenue.RevenueMilestone();
        dto.setMilestoneId("val-milestoneId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setTargetDate(LocalDate.of(2025,6,1));
        dto.setCompletedDate(LocalDate.of(2025,6,1));
        dto.setStatus(Revenue.RevenueMilestone.MilestoneStatus.PENDING);
        assertEquals("val-milestoneId", dto.getMilestoneId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getTargetDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getCompletedDate());
        assertEquals(Revenue.RevenueMilestone.MilestoneStatus.PENDING, dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        Revenue.RevenueMilestone dto1 = new Revenue.RevenueMilestone();
        Revenue.RevenueMilestone dto2 = new Revenue.RevenueMilestone();
        dto1.setMilestoneId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setTargetDate(LocalDate.of(2025,1,1));
        dto1.setCompletedDate(LocalDate.of(2025,1,1));
        dto1.setStatus(Revenue.RevenueMilestone.MilestoneStatus.PENDING);
        dto2.setMilestoneId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setTargetDate(LocalDate.of(2025,1,1));
        dto2.setCompletedDate(LocalDate.of(2025,1,1));
        dto2.setStatus(Revenue.RevenueMilestone.MilestoneStatus.PENDING);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setMilestoneId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        Revenue.RevenueMilestone dto = new Revenue.RevenueMilestone();
        dto.setMilestoneId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setTargetDate(LocalDate.of(2025,1,1));
        dto.setCompletedDate(LocalDate.of(2025,1,1));
        dto.setStatus(Revenue.RevenueMilestone.MilestoneStatus.PENDING);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        Revenue.RevenueMilestone dto = new Revenue.RevenueMilestone();
        dto.setMilestoneId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setTargetDate(LocalDate.of(2025,1,1));
        dto.setCompletedDate(LocalDate.of(2025,1,1));
        dto.setStatus(Revenue.RevenueMilestone.MilestoneStatus.PENDING);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}