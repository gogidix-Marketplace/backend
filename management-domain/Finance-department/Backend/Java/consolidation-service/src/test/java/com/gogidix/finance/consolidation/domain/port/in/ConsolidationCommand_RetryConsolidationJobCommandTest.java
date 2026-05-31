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
class ConsolidationCommand_RetryConsolidationJobCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.RetryConsolidationJobCommand dto = new ConsolidationCommand.RetryConsolidationJobCommand();
        dto.setTenantId("val-tenantId");
        dto.setJobId("val-jobId");
        dto.setRetriedBy("val-retriedBy");
        dto.setFromStep("val-fromStep");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-jobId", dto.getJobId());
        assertEquals("val-retriedBy", dto.getRetriedBy());
        assertEquals("val-fromStep", dto.getFromStep());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.RetryConsolidationJobCommand dto1 = new ConsolidationCommand.RetryConsolidationJobCommand();
        ConsolidationCommand.RetryConsolidationJobCommand dto2 = new ConsolidationCommand.RetryConsolidationJobCommand();
        dto1.setTenantId("test");
        dto1.setJobId("test");
        dto1.setRetriedBy("test");
        dto1.setFromStep("test");
        dto2.setTenantId("test");
        dto2.setJobId("test");
        dto2.setRetriedBy("test");
        dto2.setFromStep("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.RetryConsolidationJobCommand dto = new ConsolidationCommand.RetryConsolidationJobCommand();
        dto.setTenantId("test");
        dto.setJobId("test");
        dto.setRetriedBy("test");
        dto.setFromStep("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.RetryConsolidationJobCommand dto = new ConsolidationCommand.RetryConsolidationJobCommand();
        dto.setTenantId("test");
        dto.setJobId("test");
        dto.setRetriedBy("test");
        dto.setFromStep("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}