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
class PayrollCommand_ApprovePayrollCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.ApprovePayrollCommand dto = PayrollCommand.ApprovePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .approvedBy("test-approvedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.ApprovePayrollCommand dto = new PayrollCommand.ApprovePayrollCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setApprovedBy("val-approvedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.ApprovePayrollCommand dto1 = PayrollCommand.ApprovePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .approvedBy("test-approvedBy")
            .build();
        PayrollCommand.ApprovePayrollCommand dto2 = PayrollCommand.ApprovePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .approvedBy("test-approvedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.ApprovePayrollCommand dto = PayrollCommand.ApprovePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .approvedBy("test-approvedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}