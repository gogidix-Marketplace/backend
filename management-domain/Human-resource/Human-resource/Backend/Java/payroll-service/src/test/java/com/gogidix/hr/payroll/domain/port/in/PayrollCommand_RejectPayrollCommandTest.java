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
class PayrollCommand_RejectPayrollCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.RejectPayrollCommand dto = PayrollCommand.RejectPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .reason("test-reason")
            .rejectedBy("test-rejectedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-rejectedBy", dto.getRejectedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.RejectPayrollCommand dto = new PayrollCommand.RejectPayrollCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setReason("val-reason");
        dto.setRejectedBy("val-rejectedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-rejectedBy", dto.getRejectedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.RejectPayrollCommand dto1 = PayrollCommand.RejectPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .reason("test-reason")
            .rejectedBy("test-rejectedBy")
            .build();
        PayrollCommand.RejectPayrollCommand dto2 = PayrollCommand.RejectPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .reason("test-reason")
            .rejectedBy("test-rejectedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.RejectPayrollCommand dto = PayrollCommand.RejectPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .reason("test-reason")
            .rejectedBy("test-rejectedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}