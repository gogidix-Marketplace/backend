package com.gogidix.finance.ledger.domain.model;

import com.gogidix.finance.ledger.domain.model.LedgerAccount;
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
class LedgerAccount_AccountTagTest {

        @Test
    void testBuilder() {
        LedgerAccount.AccountTag dto = LedgerAccount.AccountTag.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        assertNotNull(dto);
        assertEquals("test-key", dto.getKey());
        assertEquals("test-value", dto.getValue());
    }

    @Test
    void testSettersAndGetters() {
        LedgerAccount.AccountTag dto = new LedgerAccount.AccountTag();
        dto.setKey("val-key");
        dto.setValue("val-value");
        assertEquals("val-key", dto.getKey());
        assertEquals("val-value", dto.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccount.AccountTag dto1 = LedgerAccount.AccountTag.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        LedgerAccount.AccountTag dto2 = LedgerAccount.AccountTag.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerAccount.AccountTag dto = LedgerAccount.AccountTag.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}