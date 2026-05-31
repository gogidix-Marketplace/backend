package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.domain.port.in.PayrollEntryCommand;
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
class PayrollEntryCommand_DeleteEntryCommandTest {

        @Test
    void testBuilder() {
        PayrollEntryCommand.DeleteEntryCommand dto = PayrollEntryCommand.DeleteEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .deletedBy("test-deletedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-entryId", dto.getEntryId());
        assertEquals("test-deletedBy", dto.getDeletedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollEntryCommand.DeleteEntryCommand dto = new PayrollEntryCommand.DeleteEntryCommand();
        dto.setTenantId("val-tenantId");
        dto.setEntryId("val-entryId");
        dto.setDeletedBy("val-deletedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-entryId", dto.getEntryId());
        assertEquals("val-deletedBy", dto.getDeletedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryCommand.DeleteEntryCommand dto1 = PayrollEntryCommand.DeleteEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .deletedBy("test-deletedBy")
            .build();
        PayrollEntryCommand.DeleteEntryCommand dto2 = PayrollEntryCommand.DeleteEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .deletedBy("test-deletedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollEntryCommand.DeleteEntryCommand dto = PayrollEntryCommand.DeleteEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .deletedBy("test-deletedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}