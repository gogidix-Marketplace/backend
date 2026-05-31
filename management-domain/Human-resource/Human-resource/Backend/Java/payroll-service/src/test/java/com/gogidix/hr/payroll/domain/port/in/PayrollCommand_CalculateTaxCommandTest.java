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
class PayrollCommand_CalculateTaxCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.CalculateTaxCommand dto = PayrollCommand.CalculateTaxCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .taxRuleId("test-taxRuleId")
            .employeeId("test-employeeId")
            .grossPay(BigDecimal.TEN)
            .exemptions(42)
            .ytdTax(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-taxRuleId", dto.getTaxRuleId());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals(BigDecimal.TEN, dto.getGrossPay());
        assertEquals(42, dto.getExemptions());
        assertEquals(BigDecimal.TEN, dto.getYtdTax());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.CalculateTaxCommand dto = new PayrollCommand.CalculateTaxCommand();
        dto.setTenantId("val-tenantId");
        dto.setPayrollId("val-payrollId");
        dto.setTaxRuleId("val-taxRuleId");
        dto.setEmployeeId("val-employeeId");
        dto.setGrossPay(BigDecimal.ONE);
        dto.setExemptions(99);
        dto.setYtdTax(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-taxRuleId", dto.getTaxRuleId());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals(BigDecimal.ONE, dto.getGrossPay());
        assertEquals(99, dto.getExemptions());
        assertEquals(BigDecimal.ONE, dto.getYtdTax());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.CalculateTaxCommand dto1 = PayrollCommand.CalculateTaxCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .taxRuleId("test-taxRuleId")
            .employeeId("test-employeeId")
            .grossPay(BigDecimal.TEN)
            .exemptions(42)
            .ytdTax(BigDecimal.TEN)
            .build();
        PayrollCommand.CalculateTaxCommand dto2 = PayrollCommand.CalculateTaxCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .taxRuleId("test-taxRuleId")
            .employeeId("test-employeeId")
            .grossPay(BigDecimal.TEN)
            .exemptions(42)
            .ytdTax(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.CalculateTaxCommand dto = PayrollCommand.CalculateTaxCommand.builder()
                        .tenantId("test-tenantId")
            .payrollId("test-payrollId")
            .taxRuleId("test-taxRuleId")
            .employeeId("test-employeeId")
            .grossPay(BigDecimal.TEN)
            .exemptions(42)
            .ytdTax(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}