package com.gogidix.finance.ledger.domain.port.in;

import com.gogidix.finance.ledger.domain.port.in.LedgerAccountQuery;
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
class LedgerAccountQuery_ChartOfAccountsTest {

        @Test
    void testBuilder() {
        LedgerAccountQuery.ChartOfAccounts dto = LedgerAccountQuery.ChartOfAccounts.builder()
                        .tenantId("test-tenantId")
            .generatedAt(LocalDate.of(2025,1,15))
            .accounts(Collections.emptyList())
            .totalAccounts(42)
            .activeAccounts(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,1,15), dto.getGeneratedAt());
        assertEquals(42, dto.getTotalAccounts());
        assertEquals(42, dto.getActiveAccounts());
    }

    @Test
    void testSettersAndGetters() {
        LedgerAccountQuery.ChartOfAccounts dto = new LedgerAccountQuery.ChartOfAccounts();
        dto.setTenantId("val-tenantId");
        dto.setGeneratedAt(LocalDate.of(2025,6,1));
        dto.setTotalAccounts(99);
        dto.setActiveAccounts(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getGeneratedAt());
        assertEquals(99, dto.getTotalAccounts());
        assertEquals(99, dto.getActiveAccounts());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountQuery.ChartOfAccounts dto1 = LedgerAccountQuery.ChartOfAccounts.builder()
                        .tenantId("test-tenantId")
            .generatedAt(LocalDate.of(2025,1,15))
            .accounts(Collections.emptyList())
            .totalAccounts(42)
            .activeAccounts(42)
            .build();
        LedgerAccountQuery.ChartOfAccounts dto2 = LedgerAccountQuery.ChartOfAccounts.builder()
                        .tenantId("test-tenantId")
            .generatedAt(LocalDate.of(2025,1,15))
            .accounts(Collections.emptyList())
            .totalAccounts(42)
            .activeAccounts(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerAccountQuery.ChartOfAccounts dto = LedgerAccountQuery.ChartOfAccounts.builder()
                        .tenantId("test-tenantId")
            .generatedAt(LocalDate.of(2025,1,15))
            .accounts(Collections.emptyList())
            .totalAccounts(42)
            .activeAccounts(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}