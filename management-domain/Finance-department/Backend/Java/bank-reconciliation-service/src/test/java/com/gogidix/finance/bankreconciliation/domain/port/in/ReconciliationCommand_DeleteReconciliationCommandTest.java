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
class ReconciliationCommand_DeleteReconciliationCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.DeleteReconciliationCommand dto = new ReconciliationCommand.DeleteReconciliationCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.DeleteReconciliationCommand dto1 = new ReconciliationCommand.DeleteReconciliationCommand();
        ReconciliationCommand.DeleteReconciliationCommand dto2 = new ReconciliationCommand.DeleteReconciliationCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.DeleteReconciliationCommand dto = new ReconciliationCommand.DeleteReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.DeleteReconciliationCommand dto = new ReconciliationCommand.DeleteReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}