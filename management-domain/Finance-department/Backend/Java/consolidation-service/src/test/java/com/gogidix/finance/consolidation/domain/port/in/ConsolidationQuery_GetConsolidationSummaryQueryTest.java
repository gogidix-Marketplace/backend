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
class ConsolidationQuery_GetConsolidationSummaryQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetConsolidationSummaryQuery dto = new ConsolidationQuery.GetConsolidationSummaryQuery();
        dto.setTenantId("val-tenantId");
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setBaseCurrency("val-baseCurrency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetConsolidationSummaryQuery dto1 = new ConsolidationQuery.GetConsolidationSummaryQuery();
        ConsolidationQuery.GetConsolidationSummaryQuery dto2 = new ConsolidationQuery.GetConsolidationSummaryQuery();
        dto1.setTenantId("test");
        dto1.setPeriodEnd(LocalDate.of(2025,1,1));
        dto1.setBaseCurrency("test");
        dto1.setSubsidiaryIds(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setPeriodEnd(LocalDate.of(2025,1,1));
        dto2.setBaseCurrency("test");
        dto2.setSubsidiaryIds(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetConsolidationSummaryQuery dto = new ConsolidationQuery.GetConsolidationSummaryQuery();
        dto.setTenantId("test");
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setBaseCurrency("test");
        dto.setSubsidiaryIds(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetConsolidationSummaryQuery dto = new ConsolidationQuery.GetConsolidationSummaryQuery();
        dto.setTenantId("test");
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setBaseCurrency("test");
        dto.setSubsidiaryIds(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}