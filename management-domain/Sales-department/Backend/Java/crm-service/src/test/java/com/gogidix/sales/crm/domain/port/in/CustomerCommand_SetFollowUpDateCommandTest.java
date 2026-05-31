package com.gogidix.sales.crm.domain.port.in;

import com.gogidix.sales.crm.domain.port.in.CustomerCommand;
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
class CustomerCommand_SetFollowUpDateCommandTest {

        @Test
    void testSettersAndGetters() {
        CustomerCommand.SetFollowUpDateCommand dto = new CustomerCommand.SetFollowUpDateCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setFollowUpDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals(LocalDate.of(2025,6,1), dto.getFollowUpDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerCommand.SetFollowUpDateCommand dto1 = new CustomerCommand.SetFollowUpDateCommand();
        CustomerCommand.SetFollowUpDateCommand dto2 = new CustomerCommand.SetFollowUpDateCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setFollowUpDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setFollowUpDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CustomerCommand.SetFollowUpDateCommand dto = new CustomerCommand.SetFollowUpDateCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setFollowUpDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CustomerCommand.SetFollowUpDateCommand dto = new CustomerCommand.SetFollowUpDateCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setFollowUpDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}