package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.port.in.ReconciliationQuery;
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
class ReconciliationQuery_GetUnreconciledTransactionsQueryTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationQuery.GetUnreconciledTransactionsQuery dto = new ReconciliationQuery.GetUnreconciledTransactionsQuery();
        dto.setTenantId("val-tenantId");
        dto.setStatementId("val-statementId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-statementId", dto.getStatementId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationQuery.GetUnreconciledTransactionsQuery dto1 = new ReconciliationQuery.GetUnreconciledTransactionsQuery();
        ReconciliationQuery.GetUnreconciledTransactionsQuery dto2 = new ReconciliationQuery.GetUnreconciledTransactionsQuery();
        dto1.setTenantId("test");
        dto1.setStatementId("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setStatementId("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationQuery.GetUnreconciledTransactionsQuery dto = new ReconciliationQuery.GetUnreconciledTransactionsQuery();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationQuery.GetUnreconciledTransactionsQuery dto = new ReconciliationQuery.GetUnreconciledTransactionsQuery();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}