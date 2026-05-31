package com.gogidix.finance.cashflow.domain.port.in;

import com.gogidix.finance.cashflow.domain.model.CashflowStatement;
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
class CashflowQuery_GetCashflowStatementsByStatusQueryTest {

        @Test
    void testSettersAndGetters() {
        CashflowQuery.GetCashflowStatementsByStatusQuery dto = new CashflowQuery.GetCashflowStatementsByStatusQuery();
        dto.setTenantId("val-tenantId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowQuery.GetCashflowStatementsByStatusQuery dto1 = new CashflowQuery.GetCashflowStatementsByStatusQuery();
        CashflowQuery.GetCashflowStatementsByStatusQuery dto2 = new CashflowQuery.GetCashflowStatementsByStatusQuery();
        dto1.setTenantId("test");
        dto1.setStatus(CashflowStatement.StatementStatus.DRAFT);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setStatus(CashflowStatement.StatementStatus.DRAFT);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowQuery.GetCashflowStatementsByStatusQuery dto = new CashflowQuery.GetCashflowStatementsByStatusQuery();
        dto.setTenantId("test");
        dto.setStatus(CashflowStatement.StatementStatus.DRAFT);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowQuery.GetCashflowStatementsByStatusQuery dto = new CashflowQuery.GetCashflowStatementsByStatusQuery();
        dto.setTenantId("test");
        dto.setStatus(CashflowStatement.StatementStatus.DRAFT);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}