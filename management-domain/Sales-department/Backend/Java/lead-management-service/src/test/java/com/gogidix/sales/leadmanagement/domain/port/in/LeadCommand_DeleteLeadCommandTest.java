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
class LeadCommand_DeleteLeadCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.DeleteLeadCommand dto = new LeadCommand.DeleteLeadCommand();
        dto.setTenantId("val-tenantId");
        dto.setLeadId("val-leadId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-leadId", dto.getLeadId());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.DeleteLeadCommand dto1 = new LeadCommand.DeleteLeadCommand();
        LeadCommand.DeleteLeadCommand dto2 = new LeadCommand.DeleteLeadCommand();
        dto1.setTenantId("test");
        dto1.setLeadId("test");
        dto2.setTenantId("test");
        dto2.setLeadId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.DeleteLeadCommand dto = new LeadCommand.DeleteLeadCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.DeleteLeadCommand dto = new LeadCommand.DeleteLeadCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}