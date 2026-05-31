package com.gogidix.sales.dealmanagement.domain.port.in;

import com.gogidix.sales.dealmanagement.domain.model.DealActivity;
import com.gogidix.sales.dealmanagement.domain.port.in.DealCommand;
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
class DealCommand_AddActivityCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.AddActivityCommand dto = new DealCommand.AddActivityCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        dto.setSubject("val-subject");
        dto.setDescription("val-description");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.AddActivityCommand dto1 = new DealCommand.AddActivityCommand();
        DealCommand.AddActivityCommand dto2 = new DealCommand.AddActivityCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto1.setActivityType(DealActivity.ActivityType.CALL);
        dto1.setSubject("test");
        dto1.setDescription("test");
        dto1.setDueDate(null);
        dto1.setPriority(DealActivity.Priority.LOW);
        dto2.setTenantId("test");
        dto2.setDealId("test");
        dto2.setActivityType(DealActivity.ActivityType.CALL);
        dto2.setSubject("test");
        dto2.setDescription("test");
        dto2.setDueDate(null);
        dto2.setPriority(DealActivity.Priority.LOW);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.AddActivityCommand dto = new DealCommand.AddActivityCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setActivityType(DealActivity.ActivityType.CALL);
        dto.setSubject("test");
        dto.setDescription("test");
        dto.setDueDate(null);
        dto.setPriority(DealActivity.Priority.LOW);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.AddActivityCommand dto = new DealCommand.AddActivityCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setActivityType(DealActivity.ActivityType.CALL);
        dto.setSubject("test");
        dto.setDescription("test");
        dto.setDueDate(null);
        dto.setPriority(DealActivity.Priority.LOW);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}