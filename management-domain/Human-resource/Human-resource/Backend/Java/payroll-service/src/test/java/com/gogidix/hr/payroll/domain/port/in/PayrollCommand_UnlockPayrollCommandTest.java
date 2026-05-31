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
class PayrollCommand_UnlockPayrollCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.UnlockPayrollCommand dto = PayrollCommand.UnlockPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .unlockedBy("test-unlockedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-unlockedBy", dto.getUnlockedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.UnlockPayrollCommand dto = new PayrollCommand.UnlockPayrollCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setUnlockedBy("val-unlockedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-unlockedBy", dto.getUnlockedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.UnlockPayrollCommand dto1 = PayrollCommand.UnlockPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .unlockedBy("test-unlockedBy")
            .build();
        PayrollCommand.UnlockPayrollCommand dto2 = PayrollCommand.UnlockPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .unlockedBy("test-unlockedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.UnlockPayrollCommand dto = PayrollCommand.UnlockPayrollCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .unlockedBy("test-unlockedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}