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
class ReconciliationCommand_ApproveReconciliationCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.ApproveReconciliationCommand dto = new ReconciliationCommand.ApproveReconciliationCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setApprovedBy("val-approvedBy");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.ApproveReconciliationCommand dto1 = new ReconciliationCommand.ApproveReconciliationCommand();
        ReconciliationCommand.ApproveReconciliationCommand dto2 = new ReconciliationCommand.ApproveReconciliationCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setApprovedBy("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setApprovedBy("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.ApproveReconciliationCommand dto = new ReconciliationCommand.ApproveReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setApprovedBy("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.ApproveReconciliationCommand dto = new ReconciliationCommand.ApproveReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setApprovedBy("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}