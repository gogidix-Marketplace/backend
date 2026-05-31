package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
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
class ReconciliationQuery_GetBankAccountsByTenantQueryTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationQuery.GetBankAccountsByTenantQuery dto = new ReconciliationQuery.GetBankAccountsByTenantQuery();
        dto.setTenantId("val-tenantId");
        dto.setIsPrimary(true);
        dto.setPage(99);
        dto.setSize(99);
        dto.setSortBy("val-sortBy");
        dto.setSortDirection("val-sortDirection");
        assertEquals("val-tenantId", dto.getTenantId());
        assertTrue(dto.getIsPrimary());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortDirection", dto.getSortDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationQuery.GetBankAccountsByTenantQuery dto1 = new ReconciliationQuery.GetBankAccountsByTenantQuery();
        ReconciliationQuery.GetBankAccountsByTenantQuery dto2 = new ReconciliationQuery.GetBankAccountsByTenantQuery();
        dto1.setTenantId("test");
        dto1.setStatus(BankAccount.AccountStatus.ACTIVE);
        dto1.setAccountType(BankAccount.AccountType.CHECKING);
        dto1.setIsPrimary(true);
        dto1.setPage(42);
        dto1.setSize(42);
        dto1.setSortBy("test");
        dto1.setSortDirection("test");
        dto2.setTenantId("test");
        dto2.setStatus(BankAccount.AccountStatus.ACTIVE);
        dto2.setAccountType(BankAccount.AccountType.CHECKING);
        dto2.setIsPrimary(true);
        dto2.setPage(42);
        dto2.setSize(42);
        dto2.setSortBy("test");
        dto2.setSortDirection("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationQuery.GetBankAccountsByTenantQuery dto = new ReconciliationQuery.GetBankAccountsByTenantQuery();
        dto.setTenantId("test");
        dto.setStatus(BankAccount.AccountStatus.ACTIVE);
        dto.setAccountType(BankAccount.AccountType.CHECKING);
        dto.setIsPrimary(true);
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationQuery.GetBankAccountsByTenantQuery dto = new ReconciliationQuery.GetBankAccountsByTenantQuery();
        dto.setTenantId("test");
        dto.setStatus(BankAccount.AccountStatus.ACTIVE);
        dto.setAccountType(BankAccount.AccountType.CHECKING);
        dto.setIsPrimary(true);
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}