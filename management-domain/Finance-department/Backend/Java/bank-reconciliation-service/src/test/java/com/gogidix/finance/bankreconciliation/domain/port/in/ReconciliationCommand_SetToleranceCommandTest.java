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
class ReconciliationCommand_SetToleranceCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.SetToleranceCommand dto = new ReconciliationCommand.SetToleranceCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setTolerance(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals(BigDecimal.ONE, dto.getTolerance());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.SetToleranceCommand dto1 = new ReconciliationCommand.SetToleranceCommand();
        ReconciliationCommand.SetToleranceCommand dto2 = new ReconciliationCommand.SetToleranceCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setTolerance(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setTolerance(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.SetToleranceCommand dto = new ReconciliationCommand.SetToleranceCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setTolerance(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.SetToleranceCommand dto = new ReconciliationCommand.SetToleranceCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setTolerance(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}