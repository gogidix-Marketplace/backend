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
class ReconciliationCommand_StartReconciliationCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.StartReconciliationCommand dto = new ReconciliationCommand.StartReconciliationCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setStartedBy("val-startedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals("val-startedBy", dto.getStartedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.StartReconciliationCommand dto1 = new ReconciliationCommand.StartReconciliationCommand();
        ReconciliationCommand.StartReconciliationCommand dto2 = new ReconciliationCommand.StartReconciliationCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setStartedBy("test");
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setStartedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.StartReconciliationCommand dto = new ReconciliationCommand.StartReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setStartedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.StartReconciliationCommand dto = new ReconciliationCommand.StartReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setStartedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}