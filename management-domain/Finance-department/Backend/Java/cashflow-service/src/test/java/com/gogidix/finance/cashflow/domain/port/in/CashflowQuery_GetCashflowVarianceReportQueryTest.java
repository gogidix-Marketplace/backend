package com.gogidix.finance.cashflow.domain.port.in;

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
class CashflowQuery_GetCashflowVarianceReportQueryTest {

        @Test
    void testSettersAndGetters() {
        CashflowQuery.GetCashflowVarianceReportQuery dto = new CashflowQuery.GetCashflowVarianceReportQuery();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setComparisonDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals(LocalDate.of(2025,6,1), dto.getComparisonDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQuery.GetCashflowVarianceReportQuery dto1 = new CashflowQuery.GetCashflowVarianceReportQuery();
        CashflowQuery.GetCashflowVarianceReportQuery dto2 = new CashflowQuery.GetCashflowVarianceReportQuery();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setComparisonDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setComparisonDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowQuery.GetCashflowVarianceReportQuery dto = new CashflowQuery.GetCashflowVarianceReportQuery();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setComparisonDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowQuery.GetCashflowVarianceReportQuery dto = new CashflowQuery.GetCashflowVarianceReportQuery();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setComparisonDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}