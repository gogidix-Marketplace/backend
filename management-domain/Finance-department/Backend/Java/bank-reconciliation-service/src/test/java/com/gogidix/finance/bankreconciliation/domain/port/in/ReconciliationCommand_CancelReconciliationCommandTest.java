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
class ReconciliationCommand_CancelReconciliationCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.CancelReconciliationCommand dto = new ReconciliationCommand.CancelReconciliationCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setCancelledBy("val-cancelledBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals("val-cancelledBy", dto.getCancelledBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.CancelReconciliationCommand dto1 = new ReconciliationCommand.CancelReconciliationCommand();
        ReconciliationCommand.CancelReconciliationCommand dto2 = new ReconciliationCommand.CancelReconciliationCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setCancelledBy("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setCancelledBy("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.CancelReconciliationCommand dto = new ReconciliationCommand.CancelReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setCancelledBy("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.CancelReconciliationCommand dto = new ReconciliationCommand.CancelReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setCancelledBy("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}