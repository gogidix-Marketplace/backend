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
class PayrollCommand_SubmitPayrollCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.SubmitPayrollCommand dto = PayrollCommand.SubmitPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .submittedBy("test-submittedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-submittedBy", dto.getSubmittedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.SubmitPayrollCommand dto = new PayrollCommand.SubmitPayrollCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setSubmittedBy("val-submittedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-submittedBy", dto.getSubmittedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.SubmitPayrollCommand dto1 = PayrollCommand.SubmitPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .submittedBy("test-submittedBy")
            .build();
        PayrollCommand.SubmitPayrollCommand dto2 = PayrollCommand.SubmitPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .submittedBy("test-submittedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.SubmitPayrollCommand dto = PayrollCommand.SubmitPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .submittedBy("test-submittedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}