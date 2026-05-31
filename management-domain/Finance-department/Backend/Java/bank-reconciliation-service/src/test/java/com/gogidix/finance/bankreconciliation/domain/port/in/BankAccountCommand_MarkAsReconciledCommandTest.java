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
class BankAccountCommand_MarkAsReconciledCommandTest {

        @Test
    void testSettersAndGetters() {
        BankAccountCommand.MarkAsReconciledCommand dto = new BankAccountCommand.MarkAsReconciledCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setStatementDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStatementDate());
    }

    @Test
    void testEqualsAndHashCode() {
        BankAccountCommand.MarkAsReconciledCommand dto1 = new BankAccountCommand.MarkAsReconciledCommand();
        BankAccountCommand.MarkAsReconciledCommand dto2 = new BankAccountCommand.MarkAsReconciledCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setStatementDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setStatementDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BankAccountCommand.MarkAsReconciledCommand dto = new BankAccountCommand.MarkAsReconciledCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setStatementDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BankAccountCommand.MarkAsReconciledCommand dto = new BankAccountCommand.MarkAsReconciledCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setStatementDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}