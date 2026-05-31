package com.gogidix.finance.cashflow.domain.port.in;

import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.domain.port.in.CashflowQuery;
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
class CashflowQuery_GetCashflowSummaryQueryTest {

        @Test
    void testSettersAndGetters() {
        CashflowQuery.GetCashflowSummaryQuery dto = new CashflowQuery.GetCashflowSummaryQuery();
        dto.setTenantId("val-tenantId");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setCostCenter("val-costCenter");
        dto.setProject("val-project");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-project", dto.getProject());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQuery.GetCashflowSummaryQuery dto1 = new CashflowQuery.GetCashflowSummaryQuery();
        CashflowQuery.GetCashflowSummaryQuery dto2 = new CashflowQuery.GetCashflowSummaryQuery();
        dto1.setTenantId("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setCostCenter("test");
        dto1.setProject("test");
        dto1.setType(CashflowItem.CashflowType.INFLOW);
        dto2.setTenantId("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setCostCenter("test");
        dto2.setProject("test");
        dto2.setType(CashflowItem.CashflowType.INFLOW);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowQuery.GetCashflowSummaryQuery dto = new CashflowQuery.GetCashflowSummaryQuery();
        dto.setTenantId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setCostCenter("test");
        dto.setProject("test");
        dto.setType(CashflowItem.CashflowType.INFLOW);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowQuery.GetCashflowSummaryQuery dto = new CashflowQuery.GetCashflowSummaryQuery();
        dto.setTenantId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setCostCenter("test");
        dto.setProject("test");
        dto.setType(CashflowItem.CashflowType.INFLOW);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}