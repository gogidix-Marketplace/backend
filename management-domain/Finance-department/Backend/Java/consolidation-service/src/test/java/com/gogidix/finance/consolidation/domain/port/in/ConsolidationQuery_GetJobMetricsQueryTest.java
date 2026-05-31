package com.gogidix.finance.consolidation.domain.port.in;

import com.gogidix.finance.consolidation.domain.port.in.ConsolidationQuery;
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
class ConsolidationQuery_GetJobMetricsQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetJobMetricsQuery dto = new ConsolidationQuery.GetJobMetricsQuery();
        dto.setTenantId("val-tenantId");
        dto.setJobId("val-jobId");
        dto.setIncludeSteps(true);
        dto.setIncludeLogs(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-jobId", dto.getJobId());
        assertTrue(dto.getIncludeSteps());
        assertTrue(dto.getIncludeLogs());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetJobMetricsQuery dto1 = new ConsolidationQuery.GetJobMetricsQuery();
        ConsolidationQuery.GetJobMetricsQuery dto2 = new ConsolidationQuery.GetJobMetricsQuery();
        dto1.setTenantId("test");
        dto1.setJobId("test");
        dto1.setIncludeSteps(true);
        dto1.setIncludeLogs(true);
        dto2.setTenantId("test");
        dto2.setJobId("test");
        dto2.setIncludeSteps(true);
        dto2.setIncludeLogs(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetJobMetricsQuery dto = new ConsolidationQuery.GetJobMetricsQuery();
        dto.setTenantId("test");
        dto.setJobId("test");
        dto.setIncludeSteps(true);
        dto.setIncludeLogs(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetJobMetricsQuery dto = new ConsolidationQuery.GetJobMetricsQuery();
        dto.setTenantId("test");
        dto.setJobId("test");
        dto.setIncludeSteps(true);
        dto.setIncludeLogs(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}