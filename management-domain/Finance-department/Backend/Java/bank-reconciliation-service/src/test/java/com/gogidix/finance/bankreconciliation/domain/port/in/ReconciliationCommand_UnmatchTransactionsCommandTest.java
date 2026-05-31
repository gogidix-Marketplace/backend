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
class ReconciliationCommand_UnmatchTransactionsCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.UnmatchTransactionsCommand dto = new ReconciliationCommand.UnmatchTransactionsCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationLineId("val-reconciliationLineId");
        dto.setUnmatchedBy("val-unmatchedBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationLineId", dto.getReconciliationLineId());
        assertEquals("val-unmatchedBy", dto.getUnmatchedBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.UnmatchTransactionsCommand dto1 = new ReconciliationCommand.UnmatchTransactionsCommand();
        ReconciliationCommand.UnmatchTransactionsCommand dto2 = new ReconciliationCommand.UnmatchTransactionsCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationLineId("test");
        dto1.setUnmatchedBy("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setReconciliationLineId("test");
        dto2.setUnmatchedBy("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.UnmatchTransactionsCommand dto = new ReconciliationCommand.UnmatchTransactionsCommand();
        dto.setTenantId("test");
        dto.setReconciliationLineId("test");
        dto.setUnmatchedBy("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.UnmatchTransactionsCommand dto = new ReconciliationCommand.UnmatchTransactionsCommand();
        dto.setTenantId("test");
        dto.setReconciliationLineId("test");
        dto.setUnmatchedBy("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}