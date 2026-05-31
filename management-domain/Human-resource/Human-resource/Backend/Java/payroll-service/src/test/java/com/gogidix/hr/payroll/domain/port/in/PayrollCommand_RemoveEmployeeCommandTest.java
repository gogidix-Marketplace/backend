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
class PayrollCommand_RemoveEmployeeCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.RemoveEmployeeCommand dto = PayrollCommand.RemoveEmployeeCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .removedBy("test-removedBy")
            .reason("test-reason")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-removedBy", dto.getRemovedBy());
        assertEquals("test-reason", dto.getReason());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.RemoveEmployeeCommand dto = new PayrollCommand.RemoveEmployeeCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setEmployeeId("val-employeeId");
        dto.setRemovedBy("val-removedBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-removedBy", dto.getRemovedBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.RemoveEmployeeCommand dto1 = PayrollCommand.RemoveEmployeeCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .removedBy("test-removedBy")
            .reason("test-reason")
            .build();
        PayrollCommand.RemoveEmployeeCommand dto2 = PayrollCommand.RemoveEmployeeCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .removedBy("test-removedBy")
            .reason("test-reason")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.RemoveEmployeeCommand dto = PayrollCommand.RemoveEmployeeCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .removedBy("test-removedBy")
            .reason("test-reason")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}