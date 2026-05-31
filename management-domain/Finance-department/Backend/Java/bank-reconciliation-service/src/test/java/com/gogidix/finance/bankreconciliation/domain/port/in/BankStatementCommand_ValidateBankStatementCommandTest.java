package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.port.in.BankStatementCommand;
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
class BankStatementCommand_ValidateBankStatementCommandTest {

        @Test
    void testSettersAndGetters() {
        BankStatementCommand.ValidateBankStatementCommand dto = new BankStatementCommand.ValidateBankStatementCommand();
        dto.setTenantId("val-tenantId");
        dto.setStatementId("val-statementId");
        dto.setTolerance(BigDecimal.ONE);
        dto.setValidateBalances(true);
        dto.setValidateTransactions(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-statementId", dto.getStatementId());
        assertEquals(BigDecimal.ONE, dto.getTolerance());
        assertTrue(dto.getValidateBalances());
        assertTrue(dto.getValidateTransactions());
    }

    @Test
    void testEqualsAndHashCode() {
        BankStatementCommand.ValidateBankStatementCommand dto1 = new BankStatementCommand.ValidateBankStatementCommand();
        BankStatementCommand.ValidateBankStatementCommand dto2 = new BankStatementCommand.ValidateBankStatementCommand();
        dto1.setTenantId("test");
        dto1.setStatementId("test");
        dto1.setTolerance(BigDecimal.TEN);
        dto1.setValidateBalances(true);
        dto1.setValidateTransactions(true);
        dto2.setTenantId("test");
        dto2.setStatementId("test");
        dto2.setTolerance(BigDecimal.TEN);
        dto2.setValidateBalances(true);
        dto2.setValidateTransactions(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BankStatementCommand.ValidateBankStatementCommand dto = new BankStatementCommand.ValidateBankStatementCommand();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setTolerance(BigDecimal.TEN);
        dto.setValidateBalances(true);
        dto.setValidateTransactions(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BankStatementCommand.ValidateBankStatementCommand dto = new BankStatementCommand.ValidateBankStatementCommand();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setTolerance(BigDecimal.TEN);
        dto.setValidateBalances(true);
        dto.setValidateTransactions(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}