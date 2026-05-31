package com.gogidix.finance.bankreconciliation.domain.port.in;

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
class ReconciliationQuery_GetReconciliationLinesQueryTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationQuery.GetReconciliationLinesQuery dto = new ReconciliationQuery.GetReconciliationLinesQuery();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setRequiresManualReview(true);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertTrue(dto.getRequiresManualReview());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationQuery.GetReconciliationLinesQuery dto1 = new ReconciliationQuery.GetReconciliationLinesQuery();
        ReconciliationQuery.GetReconciliationLinesQuery dto2 = new ReconciliationQuery.GetReconciliationLinesQuery();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setMatchStatus(null);
        dto1.setRequiresManualReview(true);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setMatchStatus(null);
        dto2.setRequiresManualReview(true);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationQuery.GetReconciliationLinesQuery dto = new ReconciliationQuery.GetReconciliationLinesQuery();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setMatchStatus(null);
        dto.setRequiresManualReview(true);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationQuery.GetReconciliationLinesQuery dto = new ReconciliationQuery.GetReconciliationLinesQuery();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setMatchStatus(null);
        dto.setRequiresManualReview(true);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}