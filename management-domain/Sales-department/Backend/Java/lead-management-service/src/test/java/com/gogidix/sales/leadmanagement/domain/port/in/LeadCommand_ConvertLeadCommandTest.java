package com.gogidix.sales.leadmanagement.domain.port.in;

import com.gogidix.sales.leadmanagement.domain.port.in.LeadCommand;
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
class LeadCommand_ConvertLeadCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.ConvertLeadCommand dto = new LeadCommand.ConvertLeadCommand();
        dto.setTenantId("val-tenantId");
        dto.setLeadId("val-leadId");
        dto.setDealId("val-dealId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-leadId", dto.getLeadId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.ConvertLeadCommand dto1 = new LeadCommand.ConvertLeadCommand();
        LeadCommand.ConvertLeadCommand dto2 = new LeadCommand.ConvertLeadCommand();
        dto1.setTenantId("test");
        dto1.setLeadId("test");
        dto1.setDealId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setLeadId("test");
        dto2.setDealId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.ConvertLeadCommand dto = new LeadCommand.ConvertLeadCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setDealId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.ConvertLeadCommand dto = new LeadCommand.ConvertLeadCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setDealId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}