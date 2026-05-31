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
class ConsolidationQuery_GetConsolidationReportQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetConsolidationReportQuery dto = new ConsolidationQuery.GetConsolidationReportQuery();
        dto.setTenantId("val-tenantId");
        dto.setReportId("val-reportId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportId", dto.getReportId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetConsolidationReportQuery dto1 = new ConsolidationQuery.GetConsolidationReportQuery();
        ConsolidationQuery.GetConsolidationReportQuery dto2 = new ConsolidationQuery.GetConsolidationReportQuery();
        dto1.setTenantId("test");
        dto1.setReportId("test");
        dto2.setTenantId("test");
        dto2.setReportId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetConsolidationReportQuery dto = new ConsolidationQuery.GetConsolidationReportQuery();
        dto.setTenantId("test");
        dto.setReportId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetConsolidationReportQuery dto = new ConsolidationQuery.GetConsolidationReportQuery();
        dto.setTenantId("test");
        dto.setReportId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}