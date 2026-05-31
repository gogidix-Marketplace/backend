package com.gogidix.finance.bankreconciliation.domain.port.in;

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
class BankAccountCommand_UpdateBalanceCommandTest {

        @Test
    void testSettersAndGetters() {
        BankAccountCommand.UpdateBalanceCommand dto = new BankAccountCommand.UpdateBalanceCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setNewBalance(BigDecimal.ONE);
        dto.setBalanceDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals(BigDecimal.ONE, dto.getNewBalance());
        assertEquals(LocalDate.of(2025,6,1), dto.getBalanceDate());
    }

    @Test
    void testEqualsAndHashCode() {
        BankAccountCommand.UpdateBalanceCommand dto1 = new BankAccountCommand.UpdateBalanceCommand();
        BankAccountCommand.UpdateBalanceCommand dto2 = new BankAccountCommand.UpdateBalanceCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setNewBalance(BigDecimal.TEN);
        dto1.setBalanceDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setNewBalance(BigDecimal.TEN);
        dto2.setBalanceDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BankAccountCommand.UpdateBalanceCommand dto = new BankAccountCommand.UpdateBalanceCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setNewBalance(BigDecimal.TEN);
        dto.setBalanceDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BankAccountCommand.UpdateBalanceCommand dto = new BankAccountCommand.UpdateBalanceCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setNewBalance(BigDecimal.TEN);
        dto.setBalanceDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}