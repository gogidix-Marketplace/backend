package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.domain.port.in.PayrollCommand;
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
class PayrollCommand_MarkAsPaidCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.MarkAsPaidCommand dto = PayrollCommand.MarkAsPaidCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .paymentReference("test-paymentReference")
            .processedBy("test-processedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-paymentReference", dto.getPaymentReference());
        assertEquals("test-processedBy", dto.getProcessedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.MarkAsPaidCommand dto = new PayrollCommand.MarkAsPaidCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setPaymentReference("val-paymentReference");
        dto.setProcessedBy("val-processedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-paymentReference", dto.getPaymentReference());
        assertEquals("val-processedBy", dto.getProcessedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.MarkAsPaidCommand dto1 = PayrollCommand.MarkAsPaidCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .paymentReference("test-paymentReference")
            .processedBy("test-processedBy")
            .build();
        PayrollCommand.MarkAsPaidCommand dto2 = PayrollCommand.MarkAsPaidCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .paymentReference("test-paymentReference")
            .processedBy("test-processedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.MarkAsPaidCommand dto = PayrollCommand.MarkAsPaidCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .paymentReference("test-paymentReference")
            .processedBy("test-processedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}