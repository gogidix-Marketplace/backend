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
class PayrollEntryCommand_CalculateEntryCommandTest {

        @Test
    void testBuilder() {
        PayrollEntryCommand.CalculateEntryCommand dto = PayrollEntryCommand.CalculateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .calculatedBy("test-calculatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-entryId", dto.getEntryId());
        assertEquals("test-calculatedBy", dto.getCalculatedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollEntryCommand.CalculateEntryCommand dto = new PayrollEntryCommand.CalculateEntryCommand();
        dto.setTenantId("val-tenantId");
        dto.setEntryId("val-entryId");
        dto.setCalculatedBy("val-calculatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-entryId", dto.getEntryId());
        assertEquals("val-calculatedBy", dto.getCalculatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryCommand.CalculateEntryCommand dto1 = PayrollEntryCommand.CalculateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .calculatedBy("test-calculatedBy")
            .build();
        PayrollEntryCommand.CalculateEntryCommand dto2 = PayrollEntryCommand.CalculateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .calculatedBy("test-calculatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollEntryCommand.CalculateEntryCommand dto = PayrollEntryCommand.CalculateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .calculatedBy("test-calculatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}