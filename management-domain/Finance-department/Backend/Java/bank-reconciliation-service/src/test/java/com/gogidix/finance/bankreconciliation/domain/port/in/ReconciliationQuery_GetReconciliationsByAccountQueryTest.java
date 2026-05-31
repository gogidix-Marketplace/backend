package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import com.gogidix.finance.bankreconciliation.domain.port.in.ReconciliationQuery;
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
class ReconciliationQuery_GetReconciliationsByAccountQueryTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationQuery.GetReconciliationsByAccountQuery dto = new ReconciliationQuery.GetReconciliationsByAccountQuery();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationQuery.GetReconciliationsByAccountQuery dto1 = new ReconciliationQuery.GetReconciliationsByAccountQuery();
        ReconciliationQuery.GetReconciliationsByAccountQuery dto2 = new ReconciliationQuery.GetReconciliationsByAccountQuery();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setStatus(Reconciliation.ReconciliationStatus.PENDING);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setStatus(Reconciliation.ReconciliationStatus.PENDING);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationQuery.GetReconciliationsByAccountQuery dto = new ReconciliationQuery.GetReconciliationsByAccountQuery();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setStatus(Reconciliation.ReconciliationStatus.PENDING);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationQuery.GetReconciliationsByAccountQuery dto = new ReconciliationQuery.GetReconciliationsByAccountQuery();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setStatus(Reconciliation.ReconciliationStatus.PENDING);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}