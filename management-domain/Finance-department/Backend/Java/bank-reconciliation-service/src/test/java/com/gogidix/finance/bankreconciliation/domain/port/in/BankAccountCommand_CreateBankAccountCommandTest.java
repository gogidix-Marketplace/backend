package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
import com.gogidix.finance.bankreconciliation.domain.port.in.BankAccountCommand;
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
class BankAccountCommand_CreateBankAccountCommandTest {

        @Test
    void testSettersAndGetters() {
        BankAccountCommand.CreateBankAccountCommand dto = new BankAccountCommand.CreateBankAccountCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountNumber("val-accountNumber");
        dto.setAccountName("val-accountName");
        dto.setBankName("val-bankName");
        dto.setCurrency("val-currency");
        dto.setBankCode("val-bankCode");
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setBalanceDate(LocalDate.of(2025,6,1));
        dto.setIban("val-iban");
        dto.setSwiftCode("val-swiftCode");
        dto.setRoutingNumber("val-routingNumber");
        dto.setDescription("val-description");
        dto.setReconciliationTolerance(BigDecimal.ONE);
        dto.setAutoReconcile(true);
        dto.setIsPrimary(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals("val-bankName", dto.getBankName());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-bankCode", dto.getBankCode());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals(LocalDate.of(2025,6,1), dto.getBalanceDate());
        assertEquals("val-iban", dto.getIban());
        assertEquals("val-swiftCode", dto.getSwiftCode());
        assertEquals("val-routingNumber", dto.getRoutingNumber());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getReconciliationTolerance());
        assertTrue(dto.getAutoReconcile());
        assertTrue(dto.getIsPrimary());
    }

    @Test
    void testEqualsAndHashCode() {
        BankAccountCommand.CreateBankAccountCommand dto1 = new BankAccountCommand.CreateBankAccountCommand();
        BankAccountCommand.CreateBankAccountCommand dto2 = new BankAccountCommand.CreateBankAccountCommand();
        dto1.setTenantId("test");
        dto1.setAccountNumber("test");
        dto1.setAccountName("test");
        dto1.setAccountType(BankAccount.AccountType.CHECKING);
        dto1.setBankName("test");
        dto1.setCurrency("test");
        dto1.setBankCode("test");
        dto1.setOpeningBalance(BigDecimal.TEN);
        dto1.setBalanceDate(LocalDate.of(2025,1,1));
        dto1.setIban("test");
        dto1.setSwiftCode("test");
        dto1.setRoutingNumber("test");
        dto1.setDescription("test");
        dto1.setTags(Collections.emptyList());
        dto1.setStatementFrequency(BankAccount.StatementFrequency.DAILY);
        dto1.setReconciliationTolerance(BigDecimal.TEN);
        dto1.setAutoReconcile(true);
        dto1.setIsPrimary(true);
        dto2.setTenantId("test");
        dto2.setAccountNumber("test");
        dto2.setAccountName("test");
        dto2.setAccountType(BankAccount.AccountType.CHECKING);
        dto2.setBankName("test");
        dto2.setCurrency("test");
        dto2.setBankCode("test");
        dto2.setOpeningBalance(BigDecimal.TEN);
        dto2.setBalanceDate(LocalDate.of(2025,1,1));
        dto2.setIban("test");
        dto2.setSwiftCode("test");
        dto2.setRoutingNumber("test");
        dto2.setDescription("test");
        dto2.setTags(Collections.emptyList());
        dto2.setStatementFrequency(BankAccount.StatementFrequency.DAILY);
        dto2.setReconciliationTolerance(BigDecimal.TEN);
        dto2.setAutoReconcile(true);
        dto2.setIsPrimary(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BankAccountCommand.CreateBankAccountCommand dto = new BankAccountCommand.CreateBankAccountCommand();
        dto.setTenantId("test");
        dto.setAccountNumber("test");
        dto.setAccountName("test");
        dto.setAccountType(BankAccount.AccountType.CHECKING);
        dto.setBankName("test");
        dto.setCurrency("test");
        dto.setBankCode("test");
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setBalanceDate(LocalDate.of(2025,1,1));
        dto.setIban("test");
        dto.setSwiftCode("test");
        dto.setRoutingNumber("test");
        dto.setDescription("test");
        dto.setTags(Collections.emptyList());
        dto.setStatementFrequency(BankAccount.StatementFrequency.DAILY);
        dto.setReconciliationTolerance(BigDecimal.TEN);
        dto.setAutoReconcile(true);
        dto.setIsPrimary(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BankAccountCommand.CreateBankAccountCommand dto = new BankAccountCommand.CreateBankAccountCommand();
        dto.setTenantId("test");
        dto.setAccountNumber("test");
        dto.setAccountName("test");
        dto.setAccountType(BankAccount.AccountType.CHECKING);
        dto.setBankName("test");
        dto.setCurrency("test");
        dto.setBankCode("test");
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setBalanceDate(LocalDate.of(2025,1,1));
        dto.setIban("test");
        dto.setSwiftCode("test");
        dto.setRoutingNumber("test");
        dto.setDescription("test");
        dto.setTags(Collections.emptyList());
        dto.setStatementFrequency(BankAccount.StatementFrequency.DAILY);
        dto.setReconciliationTolerance(BigDecimal.TEN);
        dto.setAutoReconcile(true);
        dto.setIsPrimary(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}