package com.gogidix.finance.ledger.domain.port.in;

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
class LedgerAccountCommand_SetOpeningBalanceCommandTest {

        @Test
    void testSettersAndGetters() {
        LedgerAccountCommand.SetOpeningBalanceCommand dto = new LedgerAccountCommand.SetOpeningBalanceCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setAsOfDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals(LocalDate.of(2025,6,1), dto.getAsOfDate());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountCommand.SetOpeningBalanceCommand dto1 = new LedgerAccountCommand.SetOpeningBalanceCommand();
        LedgerAccountCommand.SetOpeningBalanceCommand dto2 = new LedgerAccountCommand.SetOpeningBalanceCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setOpeningBalance(BigDecimal.TEN);
        dto1.setAsOfDate(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setOpeningBalance(BigDecimal.TEN);
        dto2.setAsOfDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LedgerAccountCommand.SetOpeningBalanceCommand dto = new LedgerAccountCommand.SetOpeningBalanceCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LedgerAccountCommand.SetOpeningBalanceCommand dto = new LedgerAccountCommand.SetOpeningBalanceCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}