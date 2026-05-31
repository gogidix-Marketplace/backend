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
class LedgerAccountCommand_RemoveTagCommandTest {

        @Test
    void testSettersAndGetters() {
        LedgerAccountCommand.RemoveTagCommand dto = new LedgerAccountCommand.RemoveTagCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setKey("val-key");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-key", dto.getKey());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountCommand.RemoveTagCommand dto1 = new LedgerAccountCommand.RemoveTagCommand();
        LedgerAccountCommand.RemoveTagCommand dto2 = new LedgerAccountCommand.RemoveTagCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setKey("test");
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setKey("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LedgerAccountCommand.RemoveTagCommand dto = new LedgerAccountCommand.RemoveTagCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setKey("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LedgerAccountCommand.RemoveTagCommand dto = new LedgerAccountCommand.RemoveTagCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setKey("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}