package com.gogidix.finance.ledger.domain.port.in;

import com.gogidix.finance.ledger.domain.model.LedgerAccount;
import com.gogidix.finance.ledger.domain.port.in.LedgerAccountCommand;
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
class LedgerAccountCommand_CreateAccountCommandTest {

        @Test
    void testSettersAndGetters() {
        LedgerAccountCommand.CreateAccountCommand dto = new LedgerAccountCommand.CreateAccountCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setCurrency("val-currency");
        dto.setCreatedBy("val-createdBy");
        dto.setParentAccountId("val-parentAccountId");
        dto.setAccountLevel(99);
        dto.setDescription("val-description");
        dto.setCostCenter("val-costCenter");
        dto.setDepartment("val-department");
        dto.setLocation("val-location");
        dto.setIsCashAccount(true);
        dto.setIsReconcilable(true);
        dto.setIsTaxAccount(true);
        dto.setTaxCode("val-taxCode");
        dto.setAllowsManualEntry(true);
        dto.setCreditLimit(BigDecimal.ONE);
        dto.setNotes("val-notes");
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setOpeningBalanceDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-parentAccountId", dto.getParentAccountId());
        assertEquals(99, dto.getAccountLevel());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-location", dto.getLocation());
        assertTrue(dto.getIsCashAccount());
        assertTrue(dto.getIsReconcilable());
        assertTrue(dto.getIsTaxAccount());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertTrue(dto.getAllowsManualEntry());
        assertEquals(BigDecimal.ONE, dto.getCreditLimit());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals(LocalDate.of(2025,6,1), dto.getOpeningBalanceDate());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountCommand.CreateAccountCommand dto1 = new LedgerAccountCommand.CreateAccountCommand();
        LedgerAccountCommand.CreateAccountCommand dto2 = new LedgerAccountCommand.CreateAccountCommand();
        dto1.setTenantId("test");
        dto1.setAccountNumber("test");
        dto1.setAccountName("test");
        dto1.setAccountType(LedgerAccount.AccountType.ASSET);
        dto1.setAccountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET);
        dto1.setCurrency("test");
        dto1.setCreatedBy("test");
        dto1.setParentAccountId("test");
        dto1.setAccountLevel(42);
        dto1.setDescription("test");
        dto1.setCostCenter("test");
        dto1.setDepartment("test");
        dto1.setLocation("test");
        dto1.setIsCashAccount(true);
        dto1.setIsReconcilable(true);
        dto1.setIsTaxAccount(true);
        dto1.setTaxCode("test");
        dto1.setAllowsManualEntry(true);
        dto1.setCreditLimit(BigDecimal.TEN);
        dto1.setNotes("test");
        dto1.setTags(Collections.emptyList());
        dto1.setOpeningBalance(BigDecimal.TEN);
        dto1.setOpeningBalanceDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setAccountNumber("test");
        dto2.setAccountName("test");
        dto2.setAccountType(LedgerAccount.AccountType.ASSET);
        dto2.setAccountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET);
        dto2.setCurrency("test");
        dto2.setCreatedBy("test");
        dto2.setParentAccountId("test");
        dto2.setAccountLevel(42);
        dto2.setDescription("test");
        dto2.setCostCenter("test");
        dto2.setDepartment("test");
        dto2.setLocation("test");
        dto2.setIsCashAccount(true);
        dto2.setIsReconcilable(true);
        dto2.setIsTaxAccount(true);
        dto2.setTaxCode("test");
        dto2.setAllowsManualEntry(true);
        dto2.setCreditLimit(BigDecimal.TEN);
        dto2.setNotes("test");
        dto2.setTags(Collections.emptyList());
        dto2.setOpeningBalance(BigDecimal.TEN);
        dto2.setOpeningBalanceDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LedgerAccountCommand.CreateAccountCommand dto = new LedgerAccountCommand.CreateAccountCommand();
        dto.setTenantId("test");
        dto.setAccountNumber("test");
        dto.setAccountName("test");
        dto.setAccountType(LedgerAccount.AccountType.ASSET);
        dto.setAccountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET);
        dto.setCurrency("test");
        dto.setCreatedBy("test");
        dto.setParentAccountId("test");
        dto.setAccountLevel(42);
        dto.setDescription("test");
        dto.setCostCenter("test");
        dto.setDepartment("test");
        dto.setLocation("test");
        dto.setIsCashAccount(true);
        dto.setIsReconcilable(true);
        dto.setIsTaxAccount(true);
        dto.setTaxCode("test");
        dto.setAllowsManualEntry(true);
        dto.setCreditLimit(BigDecimal.TEN);
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setOpeningBalanceDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LedgerAccountCommand.CreateAccountCommand dto = new LedgerAccountCommand.CreateAccountCommand();
        dto.setTenantId("test");
        dto.setAccountNumber("test");
        dto.setAccountName("test");
        dto.setAccountType(LedgerAccount.AccountType.ASSET);
        dto.setAccountSubType(LedgerAccount.AccountSubType.CURRENT_ASSET);
        dto.setCurrency("test");
        dto.setCreatedBy("test");
        dto.setParentAccountId("test");
        dto.setAccountLevel(42);
        dto.setDescription("test");
        dto.setCostCenter("test");
        dto.setDepartment("test");
        dto.setLocation("test");
        dto.setIsCashAccount(true);
        dto.setIsReconcilable(true);
        dto.setIsTaxAccount(true);
        dto.setTaxCode("test");
        dto.setAllowsManualEntry(true);
        dto.setCreditLimit(BigDecimal.TEN);
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setOpeningBalanceDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}