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
class LedgerAccountCommand_DeleteAccountCommandTest {

        @Test
    void testSettersAndGetters() {
        LedgerAccountCommand.DeleteAccountCommand dto = new LedgerAccountCommand.DeleteAccountCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountCommand.DeleteAccountCommand dto1 = new LedgerAccountCommand.DeleteAccountCommand();
        LedgerAccountCommand.DeleteAccountCommand dto2 = new LedgerAccountCommand.DeleteAccountCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LedgerAccountCommand.DeleteAccountCommand dto = new LedgerAccountCommand.DeleteAccountCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LedgerAccountCommand.DeleteAccountCommand dto = new LedgerAccountCommand.DeleteAccountCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}