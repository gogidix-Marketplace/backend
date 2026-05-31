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
class PayrollEntryCommand_MarkAsPaidCommandTest {

        @Test
    void testBuilder() {
        PayrollEntryCommand.MarkAsPaidCommand dto = PayrollEntryCommand.MarkAsPaidCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .paymentReference("test-paymentReference")
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-entryId", dto.getEntryId());
        assertEquals("test-paymentReference", dto.getPaymentReference());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollEntryCommand.MarkAsPaidCommand dto = new PayrollEntryCommand.MarkAsPaidCommand();
        dto.setTenantId("val-tenantId");
        dto.setEntryId("val-entryId");
        dto.setPaymentReference("val-paymentReference");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-entryId", dto.getEntryId());
        assertEquals("val-paymentReference", dto.getPaymentReference());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryCommand.MarkAsPaidCommand dto1 = PayrollEntryCommand.MarkAsPaidCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .paymentReference("test-paymentReference")
            .updatedBy("test-updatedBy")
            .build();
        PayrollEntryCommand.MarkAsPaidCommand dto2 = PayrollEntryCommand.MarkAsPaidCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .paymentReference("test-paymentReference")
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollEntryCommand.MarkAsPaidCommand dto = PayrollEntryCommand.MarkAsPaidCommand.builder()
                        .tenantId("test-tenantId")
            .entryId("test-entryId")
            .paymentReference("test-paymentReference")
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}