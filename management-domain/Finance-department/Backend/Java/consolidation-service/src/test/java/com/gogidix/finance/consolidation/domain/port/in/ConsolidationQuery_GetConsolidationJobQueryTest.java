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
class ConsolidationQuery_GetConsolidationJobQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetConsolidationJobQuery dto = new ConsolidationQuery.GetConsolidationJobQuery();
        dto.setTenantId("val-tenantId");
        dto.setJobId("val-jobId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-jobId", dto.getJobId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetConsolidationJobQuery dto1 = new ConsolidationQuery.GetConsolidationJobQuery();
        ConsolidationQuery.GetConsolidationJobQuery dto2 = new ConsolidationQuery.GetConsolidationJobQuery();
        dto1.setTenantId("test");
        dto1.setJobId("test");
        dto2.setTenantId("test");
        dto2.setJobId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetConsolidationJobQuery dto = new ConsolidationQuery.GetConsolidationJobQuery();
        dto.setTenantId("test");
        dto.setJobId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetConsolidationJobQuery dto = new ConsolidationQuery.GetConsolidationJobQuery();
        dto.setTenantId("test");
        dto.setJobId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}