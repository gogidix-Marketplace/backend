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
class LeadCommand_MarkAsLostCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.MarkAsLostCommand dto = new LeadCommand.MarkAsLostCommand();
        dto.setTenantId("val-tenantId");
        dto.setLeadId("val-leadId");
        dto.setLossReason("val-lossReason");
        dto.setLossDetails("val-lossDetails");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-leadId", dto.getLeadId());
        assertEquals("val-lossReason", dto.getLossReason());
        assertEquals("val-lossDetails", dto.getLossDetails());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.MarkAsLostCommand dto1 = new LeadCommand.MarkAsLostCommand();
        LeadCommand.MarkAsLostCommand dto2 = new LeadCommand.MarkAsLostCommand();
        dto1.setTenantId("test");
        dto1.setLeadId("test");
        dto1.setLossReason("test");
        dto1.setLossDetails("test");
        dto2.setTenantId("test");
        dto2.setLeadId("test");
        dto2.setLossReason("test");
        dto2.setLossDetails("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.MarkAsLostCommand dto = new LeadCommand.MarkAsLostCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setLossReason("test");
        dto.setLossDetails("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.MarkAsLostCommand dto = new LeadCommand.MarkAsLostCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setLossReason("test");
        dto.setLossDetails("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}