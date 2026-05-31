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
class LeadCommand_RecordInteractionCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.RecordInteractionCommand dto = new LeadCommand.RecordInteractionCommand();
        dto.setTenantId("val-tenantId");
        dto.setLeadId("val-leadId");
        dto.setEmailOpened(true);
        dto.setEmailClicked(true);
        dto.setFormName("val-formName");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-leadId", dto.getLeadId());
        assertTrue(dto.getEmailOpened());
        assertTrue(dto.getEmailClicked());
        assertEquals("val-formName", dto.getFormName());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.RecordInteractionCommand dto1 = new LeadCommand.RecordInteractionCommand();
        LeadCommand.RecordInteractionCommand dto2 = new LeadCommand.RecordInteractionCommand();
        dto1.setTenantId("test");
        dto1.setLeadId("test");
        dto1.setInteractionType(null);
        dto1.setEmailOpened(true);
        dto1.setEmailClicked(true);
        dto1.setFormName("test");
        dto2.setTenantId("test");
        dto2.setLeadId("test");
        dto2.setInteractionType(null);
        dto2.setEmailOpened(true);
        dto2.setEmailClicked(true);
        dto2.setFormName("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.RecordInteractionCommand dto = new LeadCommand.RecordInteractionCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setInteractionType(null);
        dto.setEmailOpened(true);
        dto.setEmailClicked(true);
        dto.setFormName("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.RecordInteractionCommand dto = new LeadCommand.RecordInteractionCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setInteractionType(null);
        dto.setEmailOpened(true);
        dto.setEmailClicked(true);
        dto.setFormName("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}