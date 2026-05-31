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
class LeadCommand_AdvanceStageCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.AdvanceStageCommand dto = new LeadCommand.AdvanceStageCommand();
        dto.setTenantId("val-tenantId");
        dto.setLeadId("val-leadId");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-leadId", dto.getLeadId());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.AdvanceStageCommand dto1 = new LeadCommand.AdvanceStageCommand();
        LeadCommand.AdvanceStageCommand dto2 = new LeadCommand.AdvanceStageCommand();
        dto1.setTenantId("test");
        dto1.setLeadId("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setLeadId("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.AdvanceStageCommand dto = new LeadCommand.AdvanceStageCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.AdvanceStageCommand dto = new LeadCommand.AdvanceStageCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}