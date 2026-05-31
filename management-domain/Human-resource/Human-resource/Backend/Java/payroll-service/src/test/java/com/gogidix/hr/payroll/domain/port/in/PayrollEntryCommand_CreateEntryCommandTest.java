package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
import com.gogidix.hr.payroll.domain.port.in.PayrollEntryCommand;
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
class PayrollEntryCommand_CreateEntryCommandTest {

        @Test
    void testBuilder() {
        PayrollEntryCommand.CreateEntryCommand dto = PayrollEntryCommand.CreateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .createdBy("test-createdBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-employeeName", dto.getEmployeeName());
        assertEquals("test-employeeCode", dto.getEmployeeCode());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-position", dto.getPosition());
        assertEquals(BigDecimal.TEN, dto.getBasicSalary());
        assertEquals(BigDecimal.TEN, dto.getOvertimeHours());
        assertEquals(BigDecimal.TEN, dto.getOvertimeRate());
        assertEquals(BigDecimal.TEN, dto.getBonus());
        assertEquals(BigDecimal.TEN, dto.getCommission());
        assertEquals(BigDecimal.TEN, dto.getAllowances());
        assertEquals(BigDecimal.TEN, dto.getHealthInsurance());
        assertEquals(BigDecimal.TEN, dto.getDentalInsurance());
        assertEquals(BigDecimal.TEN, dto.getRetirement401k());
        assertEquals("test-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("test-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals(42, dto.getTaxExemptions());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollEntryCommand.CreateEntryCommand dto = new PayrollEntryCommand.CreateEntryCommand();
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setPayrollId("val-payrollId");
        dto.setEmployeeId("val-employeeId");
        dto.setEmployeeName("val-employeeName");
        dto.setEmployeeCode("val-employeeCode");
        dto.setDepartment("val-department");
        dto.setPosition("val-position");
        dto.setBasicSalary(BigDecimal.ONE);
        dto.setOvertimeHours(BigDecimal.ONE);
        dto.setOvertimeRate(BigDecimal.ONE);
        dto.setBonus(BigDecimal.ONE);
        dto.setCommission(BigDecimal.ONE);
        dto.setAllowances(BigDecimal.ONE);
        dto.setHealthInsurance(BigDecimal.ONE);
        dto.setDentalInsurance(BigDecimal.ONE);
        dto.setRetirement401k(BigDecimal.ONE);
        dto.setBankAccountNumber("val-bankAccountNumber");
        dto.setBankRoutingNumber("val-bankRoutingNumber");
        dto.setTaxCode("val-taxCode");
        dto.setTaxExemptions(99);
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-employeeName", dto.getEmployeeName());
        assertEquals("val-employeeCode", dto.getEmployeeCode());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-position", dto.getPosition());
        assertEquals(BigDecimal.ONE, dto.getBasicSalary());
        assertEquals(BigDecimal.ONE, dto.getOvertimeHours());
        assertEquals(BigDecimal.ONE, dto.getOvertimeRate());
        assertEquals(BigDecimal.ONE, dto.getBonus());
        assertEquals(BigDecimal.ONE, dto.getCommission());
        assertEquals(BigDecimal.ONE, dto.getAllowances());
        assertEquals(BigDecimal.ONE, dto.getHealthInsurance());
        assertEquals(BigDecimal.ONE, dto.getDentalInsurance());
        assertEquals(BigDecimal.ONE, dto.getRetirement401k());
        assertEquals("val-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("val-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(99, dto.getTaxExemptions());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryCommand.CreateEntryCommand dto1 = PayrollEntryCommand.CreateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .createdBy("test-createdBy")
            .build();
        PayrollEntryCommand.CreateEntryCommand dto2 = PayrollEntryCommand.CreateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .createdBy("test-createdBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollEntryCommand.CreateEntryCommand dto = PayrollEntryCommand.CreateEntryCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .createdBy("test-createdBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}