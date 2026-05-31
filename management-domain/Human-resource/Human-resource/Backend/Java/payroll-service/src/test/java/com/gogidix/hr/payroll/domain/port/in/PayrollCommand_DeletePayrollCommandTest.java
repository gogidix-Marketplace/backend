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
class PayrollCommand_DeletePayrollCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.DeletePayrollCommand dto = PayrollCommand.DeletePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .deletedBy("test-deletedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-deletedBy", dto.getDeletedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.DeletePayrollCommand dto = new PayrollCommand.DeletePayrollCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setDeletedBy("val-deletedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-deletedBy", dto.getDeletedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.DeletePayrollCommand dto1 = PayrollCommand.DeletePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .deletedBy("test-deletedBy")
            .build();
        PayrollCommand.DeletePayrollCommand dto2 = PayrollCommand.DeletePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .deletedBy("test-deletedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.DeletePayrollCommand dto = PayrollCommand.DeletePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .deletedBy("test-deletedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}