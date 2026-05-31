package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.port.in.ReconciliationCommand;
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
class ReconciliationCommand_AutoReconcileCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.AutoReconcileCommand dto = new ReconciliationCommand.AutoReconcileCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setTolerance(BigDecimal.ONE);
        dto.setRequireExactAmountMatch(true);
        dto.setAllowDateVariance(true);
        dto.setDateVarianceDays(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals(BigDecimal.ONE, dto.getTolerance());
        assertTrue(dto.getRequireExactAmountMatch());
        assertTrue(dto.getAllowDateVariance());
        assertEquals(99, dto.getDateVarianceDays());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.AutoReconcileCommand dto1 = new ReconciliationCommand.AutoReconcileCommand();
        ReconciliationCommand.AutoReconcileCommand dto2 = new ReconciliationCommand.AutoReconcileCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setTolerance(BigDecimal.TEN);
        dto1.setRequireExactAmountMatch(true);
        dto1.setAllowDateVariance(true);
        dto1.setDateVarianceDays(42);
        dto1.setMinimumMatchConfidence(null);
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setTolerance(BigDecimal.TEN);
        dto2.setRequireExactAmountMatch(true);
        dto2.setAllowDateVariance(true);
        dto2.setDateVarianceDays(42);
        dto2.setMinimumMatchConfidence(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.AutoReconcileCommand dto = new ReconciliationCommand.AutoReconcileCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setTolerance(BigDecimal.TEN);
        dto.setRequireExactAmountMatch(true);
        dto.setAllowDateVariance(true);
        dto.setDateVarianceDays(42);
        dto.setMinimumMatchConfidence(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.AutoReconcileCommand dto = new ReconciliationCommand.AutoReconcileCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setTolerance(BigDecimal.TEN);
        dto.setRequireExactAmountMatch(true);
        dto.setAllowDateVariance(true);
        dto.setDateVarianceDays(42);
        dto.setMinimumMatchConfidence(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}