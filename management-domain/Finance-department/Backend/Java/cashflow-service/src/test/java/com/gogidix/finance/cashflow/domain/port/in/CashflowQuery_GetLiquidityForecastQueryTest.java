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
class CashflowQuery_GetLiquidityForecastQueryTest {

        @Test
    void testSettersAndGetters() {
        CashflowQuery.GetLiquidityForecastQuery dto = new CashflowQuery.GetLiquidityForecastQuery();
        dto.setTenantId("val-tenantId");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setHorizonDays(99);
        dto.setOpeningBalance(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getHorizonDays());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQuery.GetLiquidityForecastQuery dto1 = new CashflowQuery.GetLiquidityForecastQuery();
        CashflowQuery.GetLiquidityForecastQuery dto2 = new CashflowQuery.GetLiquidityForecastQuery();
        dto1.setTenantId("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setHorizonDays(42);
        dto1.setOpeningBalance(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setHorizonDays(42);
        dto2.setOpeningBalance(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowQuery.GetLiquidityForecastQuery dto = new CashflowQuery.GetLiquidityForecastQuery();
        dto.setTenantId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setHorizonDays(42);
        dto.setOpeningBalance(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowQuery.GetLiquidityForecastQuery dto = new CashflowQuery.GetLiquidityForecastQuery();
        dto.setTenantId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setHorizonDays(42);
        dto.setOpeningBalance(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}