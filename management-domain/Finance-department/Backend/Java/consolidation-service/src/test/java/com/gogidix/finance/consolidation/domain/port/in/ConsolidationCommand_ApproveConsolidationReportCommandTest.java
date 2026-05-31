package com.gogidix.finance.consolidation.domain.port.in;

import com.gogidix.finance.consolidation.domain.port.in.ConsolidationCommand;
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
class ConsolidationCommand_ApproveConsolidationReportCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.ApproveConsolidationReportCommand dto = new ConsolidationCommand.ApproveConsolidationReportCommand();
        dto.setTenantId("val-tenantId");
        dto.setReportId("val-reportId");
        dto.setApprovedBy("val-approvedBy");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportId", dto.getReportId());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.ApproveConsolidationReportCommand dto1 = new ConsolidationCommand.ApproveConsolidationReportCommand();
        ConsolidationCommand.ApproveConsolidationReportCommand dto2 = new ConsolidationCommand.ApproveConsolidationReportCommand();
        dto1.setTenantId("test");
        dto1.setReportId("test");
        dto1.setApprovedBy("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setReportId("test");
        dto2.setApprovedBy("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.ApproveConsolidationReportCommand dto = new ConsolidationCommand.ApproveConsolidationReportCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setApprovedBy("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.ApproveConsolidationReportCommand dto = new ConsolidationCommand.ApproveConsolidationReportCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setApprovedBy("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}