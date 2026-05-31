package com.gogidix.sales.leadmanagement.domain.port.in;

import com.gogidix.sales.leadmanagement.domain.model.Lead;
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
class LeadCommand_RegressStageCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.RegressStageCommand dto = new LeadCommand.RegressStageCommand();
        dto.setTenantId("val-tenantId");
        dto.setLeadId("val-leadId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-leadId", dto.getLeadId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.RegressStageCommand dto1 = new LeadCommand.RegressStageCommand();
        LeadCommand.RegressStageCommand dto2 = new LeadCommand.RegressStageCommand();
        dto1.setTenantId("test");
        dto1.setLeadId("test");
        dto1.setTargetStage(Lead.LeadStage.NEW);
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setLeadId("test");
        dto2.setTargetStage(Lead.LeadStage.NEW);
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.RegressStageCommand dto = new LeadCommand.RegressStageCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setTargetStage(Lead.LeadStage.NEW);
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.RegressStageCommand dto = new LeadCommand.RegressStageCommand();
        dto.setTenantId("test");
        dto.setLeadId("test");
        dto.setTargetStage(Lead.LeadStage.NEW);
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}