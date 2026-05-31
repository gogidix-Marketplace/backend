package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.port.in.BudgetTrackingQuery;
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
class BudgetTrackingQuery_GetTransactionQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.GetTransactionQuery dto = new BudgetTrackingQuery.GetTransactionQuery();
        dto.setTenantId("val-tenantId");
        dto.setTransactionId("val-transactionId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-transactionId", dto.getTransactionId());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingQuery.GetTransactionQuery dto1 = new BudgetTrackingQuery.GetTransactionQuery();
        BudgetTrackingQuery.GetTransactionQuery dto2 = new BudgetTrackingQuery.GetTransactionQuery();
        dto1.setTenantId("test");
        dto1.setTransactionId("test");
        dto2.setTenantId("test");
        dto2.setTransactionId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTrackingQuery.GetTransactionQuery dto = new BudgetTrackingQuery.GetTransactionQuery();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTrackingQuery.GetTransactionQuery dto = new BudgetTrackingQuery.GetTransactionQuery();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}