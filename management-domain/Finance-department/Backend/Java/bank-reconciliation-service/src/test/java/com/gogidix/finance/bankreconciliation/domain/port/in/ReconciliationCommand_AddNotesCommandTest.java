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
class ReconciliationCommand_AddNotesCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.AddNotesCommand dto = new ReconciliationCommand.AddNotesCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.AddNotesCommand dto1 = new ReconciliationCommand.AddNotesCommand();
        ReconciliationCommand.AddNotesCommand dto2 = new ReconciliationCommand.AddNotesCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.AddNotesCommand dto = new ReconciliationCommand.AddNotesCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.AddNotesCommand dto = new ReconciliationCommand.AddNotesCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}