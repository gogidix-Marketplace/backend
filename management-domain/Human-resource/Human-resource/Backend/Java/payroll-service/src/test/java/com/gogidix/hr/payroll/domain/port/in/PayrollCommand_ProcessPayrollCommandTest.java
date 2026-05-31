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
class PayrollCommand_ProcessPayrollCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.ProcessPayrollCommand dto = PayrollCommand.ProcessPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .processedBy("test-processedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-processedBy", dto.getProcessedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.ProcessPayrollCommand dto = new PayrollCommand.ProcessPayrollCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setProcessedBy("val-processedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-processedBy", dto.getProcessedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.ProcessPayrollCommand dto1 = PayrollCommand.ProcessPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .processedBy("test-processedBy")
            .build();
        PayrollCommand.ProcessPayrollCommand dto2 = PayrollCommand.ProcessPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .processedBy("test-processedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.ProcessPayrollCommand dto = PayrollCommand.ProcessPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .processedBy("test-processedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}