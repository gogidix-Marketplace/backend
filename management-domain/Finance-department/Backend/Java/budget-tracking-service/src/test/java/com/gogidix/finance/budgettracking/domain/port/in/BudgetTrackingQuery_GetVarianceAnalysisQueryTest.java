package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.port.in.BudgetTrackingQuery;
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
class BudgetTrackingQuery_GetVarianceAnalysisQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.GetVarianceAnalysisQuery dto = new BudgetTrackingQuery.GetVarianceAnalysisQuery();
        dto.setTenantId("val-tenantId");
        dto.setBudgetId("val-budgetId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-budgetId", dto.getBudgetId());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingQuery.GetVarianceAnalysisQuery dto1 = new BudgetTrackingQuery.GetVarianceAnalysisQuery();
        BudgetTrackingQuery.GetVarianceAnalysisQuery dto2 = new BudgetTrackingQuery.GetVarianceAnalysisQuery();
        dto1.setTenantId("test");
        dto1.setBudgetId("test");
        dto1.setPeriod(null);
        dto2.setTenantId("test");
        dto2.setBudgetId("test");
        dto2.setPeriod(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTrackingQuery.GetVarianceAnalysisQuery dto = new BudgetTrackingQuery.GetVarianceAnalysisQuery();
        dto.setTenantId("test");
        dto.setBudgetId("test");
        dto.setPeriod(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTrackingQuery.GetVarianceAnalysisQuery dto = new BudgetTrackingQuery.GetVarianceAnalysisQuery();
        dto.setTenantId("test");
        dto.setBudgetId("test");
        dto.setPeriod(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}