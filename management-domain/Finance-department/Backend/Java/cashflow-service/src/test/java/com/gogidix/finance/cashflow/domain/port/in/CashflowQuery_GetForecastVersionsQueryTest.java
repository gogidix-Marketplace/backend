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
class CashflowQuery_GetForecastVersionsQueryTest {

        @Test
    void testSettersAndGetters() {
        CashflowQuery.GetForecastVersionsQuery dto = new CashflowQuery.GetForecastVersionsQuery();
        dto.setTenantId("val-tenantId");
        dto.setParentForecastId("val-parentForecastId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-parentForecastId", dto.getParentForecastId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQuery.GetForecastVersionsQuery dto1 = new CashflowQuery.GetForecastVersionsQuery();
        CashflowQuery.GetForecastVersionsQuery dto2 = new CashflowQuery.GetForecastVersionsQuery();
        dto1.setTenantId("test");
        dto1.setParentForecastId("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setParentForecastId("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowQuery.GetForecastVersionsQuery dto = new CashflowQuery.GetForecastVersionsQuery();
        dto.setTenantId("test");
        dto.setParentForecastId("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowQuery.GetForecastVersionsQuery dto = new CashflowQuery.GetForecastVersionsQuery();
        dto.setTenantId("test");
        dto.setParentForecastId("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}