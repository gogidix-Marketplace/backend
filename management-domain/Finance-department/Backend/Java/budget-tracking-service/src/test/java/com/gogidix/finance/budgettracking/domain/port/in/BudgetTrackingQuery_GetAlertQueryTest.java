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
class BudgetTrackingQuery_GetAlertQueryTest {

        @Test
    void testSettersAndGetters() {
        BudgetTrackingQuery.GetAlertQuery dto = new BudgetTrackingQuery.GetAlertQuery();
        dto.setTenantId("val-tenantId");
        dto.setAlertId("val-alertId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-alertId", dto.getAlertId());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTrackingQuery.GetAlertQuery dto1 = new BudgetTrackingQuery.GetAlertQuery();
        BudgetTrackingQuery.GetAlertQuery dto2 = new BudgetTrackingQuery.GetAlertQuery();
        dto1.setTenantId("test");
        dto1.setAlertId("test");
        dto2.setTenantId("test");
        dto2.setAlertId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTrackingQuery.GetAlertQuery dto = new BudgetTrackingQuery.GetAlertQuery();
        dto.setTenantId("test");
        dto.setAlertId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTrackingQuery.GetAlertQuery dto = new BudgetTrackingQuery.GetAlertQuery();
        dto.setTenantId("test");
        dto.setAlertId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}