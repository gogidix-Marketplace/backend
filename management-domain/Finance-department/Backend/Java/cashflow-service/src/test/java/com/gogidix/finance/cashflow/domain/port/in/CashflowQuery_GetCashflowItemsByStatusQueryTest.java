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
class CashflowQuery_GetCashflowItemsByStatusQueryTest {

        @Test
    void testSettersAndGetters() {
        CashflowQuery.GetCashflowItemsByStatusQuery dto = new CashflowQuery.GetCashflowItemsByStatusQuery();
        dto.setTenantId("val-tenantId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQuery.GetCashflowItemsByStatusQuery dto1 = new CashflowQuery.GetCashflowItemsByStatusQuery();
        CashflowQuery.GetCashflowItemsByStatusQuery dto2 = new CashflowQuery.GetCashflowItemsByStatusQuery();
        dto1.setTenantId("test");
        dto1.setStatus(CashflowItem.ItemStatus.PENDING);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setStatus(CashflowItem.ItemStatus.PENDING);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowQuery.GetCashflowItemsByStatusQuery dto = new CashflowQuery.GetCashflowItemsByStatusQuery();
        dto.setTenantId("test");
        dto.setStatus(CashflowItem.ItemStatus.PENDING);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowQuery.GetCashflowItemsByStatusQuery dto = new CashflowQuery.GetCashflowItemsByStatusQuery();
        dto.setTenantId("test");
        dto.setStatus(CashflowItem.ItemStatus.PENDING);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}