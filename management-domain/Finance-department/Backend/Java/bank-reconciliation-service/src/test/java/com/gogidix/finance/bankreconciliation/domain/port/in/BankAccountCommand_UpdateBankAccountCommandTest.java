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
class BankAccountCommand_UpdateBankAccountCommandTest {

        @Test
    void testSettersAndGetters() {
        BankAccountCommand.UpdateBankAccountCommand dto = new BankAccountCommand.UpdateBankAccountCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setAccountName("val-accountName");
        dto.setDescription("val-description");
        dto.setBalance(BigDecimal.ONE);
        dto.setBalanceDate(LocalDate.of(2025,6,1));
        dto.setIban("val-iban");
        dto.setSwiftCode("val-swiftCode");
        dto.setRoutingNumber("val-routingNumber");
        dto.setReconciliationTolerance(BigDecimal.ONE);
        dto.setAutoReconcile(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getBalance());
        assertEquals(LocalDate.of(2025,6,1), dto.getBalanceDate());
        assertEquals("val-iban", dto.getIban());
        assertEquals("val-swiftCode", dto.getSwiftCode());
        assertEquals("val-routingNumber", dto.getRoutingNumber());
        assertEquals(BigDecimal.ONE, dto.getReconciliationTolerance());
        assertTrue(dto.getAutoReconcile());
    }

    @Test
    void testEqualsAndHashCode() {
        BankAccountCommand.UpdateBankAccountCommand dto1 = new BankAccountCommand.UpdateBankAccountCommand();
        BankAccountCommand.UpdateBankAccountCommand dto2 = new BankAccountCommand.UpdateBankAccountCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setAccountName("test");
        dto1.setDescription("test");
        dto1.setBalance(BigDecimal.TEN);
        dto1.setBalanceDate(LocalDate.of(2025,1,1));
        dto1.setIban("test");
        dto1.setSwiftCode("test");
        dto1.setRoutingNumber("test");
        dto1.setTags(Collections.emptyList());
        dto1.setReconciliationTolerance(BigDecimal.TEN);
        dto1.setStatementFrequency(BankAccount.StatementFrequency.DAILY);
        dto1.setAutoReconcile(true);
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setAccountName("test");
        dto2.setDescription("test");
        dto2.setBalance(BigDecimal.TEN);
        dto2.setBalanceDate(LocalDate.of(2025,1,1));
        dto2.setIban("test");
        dto2.setSwiftCode("test");
        dto2.setRoutingNumber("test");
        dto2.setTags(Collections.emptyList());
        dto2.setReconciliationTolerance(BigDecimal.TEN);
        dto2.setStatementFrequency(BankAccount.StatementFrequency.DAILY);
        dto2.setAutoReconcile(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BankAccountCommand.UpdateBankAccountCommand dto = new BankAccountCommand.UpdateBankAccountCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setAccountName("test");
        dto.setDescription("test");
        dto.setBalance(BigDecimal.TEN);
        dto.setBalanceDate(LocalDate.of(2025,1,1));
        dto.setIban("test");
        dto.setSwiftCode("test");
        dto.setRoutingNumber("test");
        dto.setTags(Collections.emptyList());
        dto.setReconciliationTolerance(BigDecimal.TEN);
        dto.setStatementFrequency(BankAccount.StatementFrequency.DAILY);
        dto.setAutoReconcile(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BankAccountCommand.UpdateBankAccountCommand dto = new BankAccountCommand.UpdateBankAccountCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setAccountName("test");
        dto.setDescription("test");
        dto.setBalance(BigDecimal.TEN);
        dto.setBalanceDate(LocalDate.of(2025,1,1));
        dto.setIban("test");
        dto.setSwiftCode("test");
        dto.setRoutingNumber("test");
        dto.setTags(Collections.emptyList());
        dto.setReconciliationTolerance(BigDecimal.TEN);
        dto.setStatementFrequency(BankAccount.StatementFrequency.DAILY);
        dto.setAutoReconcile(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}