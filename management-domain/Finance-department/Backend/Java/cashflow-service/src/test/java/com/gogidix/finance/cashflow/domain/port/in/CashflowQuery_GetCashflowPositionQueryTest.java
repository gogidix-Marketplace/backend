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
class CashflowQuery_GetCashflowPositionQueryTest {

        @Test
    void testSettersAndGetters() {
        CashflowQuery.GetCashflowPositionQuery dto = new CashflowQuery.GetCashflowPositionQuery();
        dto.setTenantId("val-tenantId");
        dto.setAsOfDate(LocalDate.of(2025,6,1));
        dto.setCurrency("val-currency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getAsOfDate());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQuery.GetCashflowPositionQuery dto1 = new CashflowQuery.GetCashflowPositionQuery();
        CashflowQuery.GetCashflowPositionQuery dto2 = new CashflowQuery.GetCashflowPositionQuery();
        dto1.setTenantId("test");
        dto1.setAsOfDate(LocalDate.of(2025,1,1));
        dto1.setCurrency("test");
        dto2.setTenantId("test");
        dto2.setAsOfDate(LocalDate.of(2025,1,1));
        dto2.setCurrency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowQuery.GetCashflowPositionQuery dto = new CashflowQuery.GetCashflowPositionQuery();
        dto.setTenantId("test");
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        dto.setCurrency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowQuery.GetCashflowPositionQuery dto = new CashflowQuery.GetCashflowPositionQuery();
        dto.setTenantId("test");
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        dto.setCurrency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}