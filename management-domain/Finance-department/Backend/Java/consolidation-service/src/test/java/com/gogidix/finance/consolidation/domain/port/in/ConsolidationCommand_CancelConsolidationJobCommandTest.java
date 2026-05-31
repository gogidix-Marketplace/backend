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
class ConsolidationCommand_CancelConsolidationJobCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.CancelConsolidationJobCommand dto = new ConsolidationCommand.CancelConsolidationJobCommand();
        dto.setTenantId("val-tenantId");
        dto.setJobId("val-jobId");
        dto.setCancelledBy("val-cancelledBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-jobId", dto.getJobId());
        assertEquals("val-cancelledBy", dto.getCancelledBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.CancelConsolidationJobCommand dto1 = new ConsolidationCommand.CancelConsolidationJobCommand();
        ConsolidationCommand.CancelConsolidationJobCommand dto2 = new ConsolidationCommand.CancelConsolidationJobCommand();
        dto1.setTenantId("test");
        dto1.setJobId("test");
        dto1.setCancelledBy("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setJobId("test");
        dto2.setCancelledBy("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.CancelConsolidationJobCommand dto = new ConsolidationCommand.CancelConsolidationJobCommand();
        dto.setTenantId("test");
        dto.setJobId("test");
        dto.setCancelledBy("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.CancelConsolidationJobCommand dto = new ConsolidationCommand.CancelConsolidationJobCommand();
        dto.setTenantId("test");
        dto.setJobId("test");
        dto.setCancelledBy("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}