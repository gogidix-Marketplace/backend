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
class ReconciliationQuery_SearchBankTransactionsQueryTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationQuery.SearchBankTransactionsQuery dto = new ReconciliationQuery.SearchBankTransactionsQuery();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setSearchTerm("val-searchTerm");
        dto.setMinAmount(BigDecimal.ONE);
        dto.setMaxAmount(BigDecimal.ONE);
        dto.setIsReconciled(true);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-searchTerm", dto.getSearchTerm());
        assertEquals(BigDecimal.ONE, dto.getMinAmount());
        assertEquals(BigDecimal.ONE, dto.getMaxAmount());
        assertTrue(dto.getIsReconciled());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationQuery.SearchBankTransactionsQuery dto1 = new ReconciliationQuery.SearchBankTransactionsQuery();
        ReconciliationQuery.SearchBankTransactionsQuery dto2 = new ReconciliationQuery.SearchBankTransactionsQuery();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setSearchTerm("test");
        dto1.setMinAmount(BigDecimal.TEN);
        dto1.setMaxAmount(BigDecimal.TEN);
        dto1.setTransactionType(null);
        dto1.setIsReconciled(true);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setSearchTerm("test");
        dto2.setMinAmount(BigDecimal.TEN);
        dto2.setMaxAmount(BigDecimal.TEN);
        dto2.setTransactionType(null);
        dto2.setIsReconciled(true);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationQuery.SearchBankTransactionsQuery dto = new ReconciliationQuery.SearchBankTransactionsQuery();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setSearchTerm("test");
        dto.setMinAmount(BigDecimal.TEN);
        dto.setMaxAmount(BigDecimal.TEN);
        dto.setTransactionType(null);
        dto.setIsReconciled(true);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationQuery.SearchBankTransactionsQuery dto = new ReconciliationQuery.SearchBankTransactionsQuery();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setSearchTerm("test");
        dto.setMinAmount(BigDecimal.TEN);
        dto.setMaxAmount(BigDecimal.TEN);
        dto.setTransactionType(null);
        dto.setIsReconciled(true);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}