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
class CashflowQuery_GetBaselineForecastQueryTest {

        @Test
    void testSettersAndGetters() {
        CashflowQuery.GetBaselineForecastQuery dto = new CashflowQuery.GetBaselineForecastQuery();
        dto.setTenantId("val-tenantId");
        dto.setDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQuery.GetBaselineForecastQuery dto1 = new CashflowQuery.GetBaselineForecastQuery();
        CashflowQuery.GetBaselineForecastQuery dto2 = new CashflowQuery.GetBaselineForecastQuery();
        dto1.setTenantId("test");
        dto1.setDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowQuery.GetBaselineForecastQuery dto = new CashflowQuery.GetBaselineForecastQuery();
        dto.setTenantId("test");
        dto.setDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowQuery.GetBaselineForecastQuery dto = new CashflowQuery.GetBaselineForecastQuery();
        dto.setTenantId("test");
        dto.setDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}