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
class ReconciliationCommand_CompleteReconciliationCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.CompleteReconciliationCommand dto = new ReconciliationCommand.CompleteReconciliationCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setReconciledBy("val-reconciledBy");
        dto.setBookBalance(BigDecimal.ONE);
        dto.setBankBalance(BigDecimal.ONE);
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals("val-reconciledBy", dto.getReconciledBy());
        assertEquals(BigDecimal.ONE, dto.getBookBalance());
        assertEquals(BigDecimal.ONE, dto.getBankBalance());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.CompleteReconciliationCommand dto1 = new ReconciliationCommand.CompleteReconciliationCommand();
        ReconciliationCommand.CompleteReconciliationCommand dto2 = new ReconciliationCommand.CompleteReconciliationCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setReconciledBy("test");
        dto1.setBookBalance(BigDecimal.TEN);
        dto1.setBankBalance(BigDecimal.TEN);
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setReconciledBy("test");
        dto2.setBookBalance(BigDecimal.TEN);
        dto2.setBankBalance(BigDecimal.TEN);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.CompleteReconciliationCommand dto = new ReconciliationCommand.CompleteReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setReconciledBy("test");
        dto.setBookBalance(BigDecimal.TEN);
        dto.setBankBalance(BigDecimal.TEN);
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.CompleteReconciliationCommand dto = new ReconciliationCommand.CompleteReconciliationCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setReconciledBy("test");
        dto.setBookBalance(BigDecimal.TEN);
        dto.setBankBalance(BigDecimal.TEN);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}