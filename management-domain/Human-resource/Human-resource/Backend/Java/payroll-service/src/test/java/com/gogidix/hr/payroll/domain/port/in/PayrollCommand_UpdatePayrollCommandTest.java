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
class PayrollCommand_UpdatePayrollCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.UpdatePayrollCommand dto = PayrollCommand.UpdatePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .payrollName("test-payrollName")
            .paymentDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-payrollName", dto.getPayrollName());
        assertEquals(LocalDate.of(2025,1,15), dto.getPaymentDate());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.UpdatePayrollCommand dto = new PayrollCommand.UpdatePayrollCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setPayrollName("val-payrollName");
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-payrollName", dto.getPayrollName());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.UpdatePayrollCommand dto1 = PayrollCommand.UpdatePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .payrollName("test-payrollName")
            .paymentDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .updatedBy("test-updatedBy")
            .build();
        PayrollCommand.UpdatePayrollCommand dto2 = PayrollCommand.UpdatePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .payrollName("test-payrollName")
            .paymentDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.UpdatePayrollCommand dto = PayrollCommand.UpdatePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .payrollName("test-payrollName")
            .paymentDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}