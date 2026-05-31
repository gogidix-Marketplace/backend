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
class PayrollCommand_GeneratePayslipsCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.GeneratePayslipsCommand dto = PayrollCommand.GeneratePayslipsCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .generatedBy("test-generatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-generatedBy", dto.getGeneratedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.GeneratePayslipsCommand dto = new PayrollCommand.GeneratePayslipsCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setGeneratedBy("val-generatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.GeneratePayslipsCommand dto1 = PayrollCommand.GeneratePayslipsCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .generatedBy("test-generatedBy")
            .build();
        PayrollCommand.GeneratePayslipsCommand dto2 = PayrollCommand.GeneratePayslipsCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .generatedBy("test-generatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.GeneratePayslipsCommand dto = PayrollCommand.GeneratePayslipsCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .generatedBy("test-generatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}