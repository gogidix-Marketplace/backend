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
class ConsolidationCommand_RejectConsolidationReportCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.RejectConsolidationReportCommand dto = new ConsolidationCommand.RejectConsolidationReportCommand();
        dto.setTenantId("val-tenantId");
        dto.setReportId("val-reportId");
        dto.setRejectedBy("val-rejectedBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportId", dto.getReportId());
        assertEquals("val-rejectedBy", dto.getRejectedBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.RejectConsolidationReportCommand dto1 = new ConsolidationCommand.RejectConsolidationReportCommand();
        ConsolidationCommand.RejectConsolidationReportCommand dto2 = new ConsolidationCommand.RejectConsolidationReportCommand();
        dto1.setTenantId("test");
        dto1.setReportId("test");
        dto1.setRejectedBy("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setReportId("test");
        dto2.setRejectedBy("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.RejectConsolidationReportCommand dto = new ConsolidationCommand.RejectConsolidationReportCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setRejectedBy("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.RejectConsolidationReportCommand dto = new ConsolidationCommand.RejectConsolidationReportCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setRejectedBy("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}