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
class PayrollCommand_AddEmployeeCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.AddEmployeeCommand dto = PayrollCommand.AddEmployeeCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .addedBy("test-addedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-addedBy", dto.getAddedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.AddEmployeeCommand dto = new PayrollCommand.AddEmployeeCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setEmployeeId("val-employeeId");
        dto.setAddedBy("val-addedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-addedBy", dto.getAddedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.AddEmployeeCommand dto1 = PayrollCommand.AddEmployeeCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .addedBy("test-addedBy")
            .build();
        PayrollCommand.AddEmployeeCommand dto2 = PayrollCommand.AddEmployeeCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .addedBy("test-addedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.AddEmployeeCommand dto = PayrollCommand.AddEmployeeCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .addedBy("test-addedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}