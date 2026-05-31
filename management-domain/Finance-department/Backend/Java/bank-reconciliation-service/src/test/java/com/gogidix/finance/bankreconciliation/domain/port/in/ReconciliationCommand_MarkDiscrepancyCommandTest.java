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
class ReconciliationCommand_MarkDiscrepancyCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.MarkDiscrepancyCommand dto = new ReconciliationCommand.MarkDiscrepancyCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationLineId("val-reconciliationLineId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationLineId", dto.getReconciliationLineId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.MarkDiscrepancyCommand dto1 = new ReconciliationCommand.MarkDiscrepancyCommand();
        ReconciliationCommand.MarkDiscrepancyCommand dto2 = new ReconciliationCommand.MarkDiscrepancyCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationLineId("test");
        dto1.setDiscrepancyCategory(null);
        dto1.setReason("test");
        dto1.setActionRequired(null);
        dto2.setTenantId("test");
        dto2.setReconciliationLineId("test");
        dto2.setDiscrepancyCategory(null);
        dto2.setReason("test");
        dto2.setActionRequired(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.MarkDiscrepancyCommand dto = new ReconciliationCommand.MarkDiscrepancyCommand();
        dto.setTenantId("test");
        dto.setReconciliationLineId("test");
        dto.setDiscrepancyCategory(null);
        dto.setReason("test");
        dto.setActionRequired(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.MarkDiscrepancyCommand dto = new ReconciliationCommand.MarkDiscrepancyCommand();
        dto.setTenantId("test");
        dto.setReconciliationLineId("test");
        dto.setDiscrepancyCategory(null);
        dto.setReason("test");
        dto.setActionRequired(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}