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
class PayrollCommand_CancelPayrollCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.CancelPayrollCommand dto = PayrollCommand.CancelPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .reason("test-reason")
            .cancelledBy("test-cancelledBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-cancelledBy", dto.getCancelledBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.CancelPayrollCommand dto = new PayrollCommand.CancelPayrollCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setReason("val-reason");
        dto.setCancelledBy("val-cancelledBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-cancelledBy", dto.getCancelledBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.CancelPayrollCommand dto1 = PayrollCommand.CancelPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .reason("test-reason")
            .cancelledBy("test-cancelledBy")
            .build();
        PayrollCommand.CancelPayrollCommand dto2 = PayrollCommand.CancelPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .reason("test-reason")
            .cancelledBy("test-cancelledBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.CancelPayrollCommand dto = PayrollCommand.CancelPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .reason("test-reason")
            .cancelledBy("test-cancelledBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}