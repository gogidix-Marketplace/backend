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
class LedgerAccountCommand_AddTagCommandTest {

        @Test
    void testSettersAndGetters() {
        LedgerAccountCommand.AddTagCommand dto = new LedgerAccountCommand.AddTagCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setKey("val-key");
        dto.setValue("val-value");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-key", dto.getKey());
        assertEquals("val-value", dto.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountCommand.AddTagCommand dto1 = new LedgerAccountCommand.AddTagCommand();
        LedgerAccountCommand.AddTagCommand dto2 = new LedgerAccountCommand.AddTagCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setKey("test");
        dto1.setValue("test");
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setKey("test");
        dto2.setValue("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LedgerAccountCommand.AddTagCommand dto = new LedgerAccountCommand.AddTagCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setKey("test");
        dto.setValue("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LedgerAccountCommand.AddTagCommand dto = new LedgerAccountCommand.AddTagCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setKey("test");
        dto.setValue("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}