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
class ConsolidationQuery_GetFinancialStatementsQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetFinancialStatementsQuery dto = new ConsolidationQuery.GetFinancialStatementsQuery();
        dto.setTenantId("val-tenantId");
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setBaseCurrency("val-baseCurrency");
        dto.setIncludeComparatives(true);
        dto.setComparativePeriods(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertTrue(dto.getIncludeComparatives());
        assertEquals(99, dto.getComparativePeriods());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetFinancialStatementsQuery dto1 = new ConsolidationQuery.GetFinancialStatementsQuery();
        ConsolidationQuery.GetFinancialStatementsQuery dto2 = new ConsolidationQuery.GetFinancialStatementsQuery();
        dto1.setTenantId("test");
        dto1.setPeriodEnd(LocalDate.of(2025,1,1));
        dto1.setPeriodStart(LocalDate.of(2025,1,1));
        dto1.setBaseCurrency("test");
        dto1.setSubsidiaryIds(Collections.emptyList());
        dto1.setIncludeComparatives(true);
        dto1.setComparativePeriods(42);
        dto2.setTenantId("test");
        dto2.setPeriodEnd(LocalDate.of(2025,1,1));
        dto2.setPeriodStart(LocalDate.of(2025,1,1));
        dto2.setBaseCurrency("test");
        dto2.setSubsidiaryIds(Collections.emptyList());
        dto2.setIncludeComparatives(true);
        dto2.setComparativePeriods(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetFinancialStatementsQuery dto = new ConsolidationQuery.GetFinancialStatementsQuery();
        dto.setTenantId("test");
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setBaseCurrency("test");
        dto.setSubsidiaryIds(Collections.emptyList());
        dto.setIncludeComparatives(true);
        dto.setComparativePeriods(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetFinancialStatementsQuery dto = new ConsolidationQuery.GetFinancialStatementsQuery();
        dto.setTenantId("test");
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setBaseCurrency("test");
        dto.setSubsidiaryIds(Collections.emptyList());
        dto.setIncludeComparatives(true);
        dto.setComparativePeriods(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}