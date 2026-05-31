package com.gogidix.hr.payroll.interfaces.rest;

import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
import com.gogidix.hr.payroll.interfaces.rest.PayrollEntryController;
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
class PayrollEntryController_CreateEntryRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        PayrollEntryController.CreateEntryRequestDto dto = new PayrollEntryController.CreateEntryRequestDto();
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
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryController.CreateEntryRequestDto dto1 = new PayrollEntryController.CreateEntryRequestDto();
        PayrollEntryController.CreateEntryRequestDto dto2 = new PayrollEntryController.CreateEntryRequestDto();
        dto1.setCountryCode("test");
        dto1.setPayrollId("test");
        dto1.setEmployeeId("test");
        dto1.setEmployeeName("test");
        dto1.setEmployeeCode("test");
        dto1.setDepartment("test");
        dto1.setPosition("test");
        dto1.setBasicSalary(BigDecimal.TEN);
        dto1.setOvertimeHours(BigDecimal.TEN);
        dto1.setOvertimeRate(BigDecimal.TEN);
        dto1.setBonus(BigDecimal.TEN);
        dto1.setCommission(BigDecimal.TEN);
        dto1.setAllowances(BigDecimal.TEN);
        dto1.setHealthInsurance(BigDecimal.TEN);
        dto1.setDentalInsurance(BigDecimal.TEN);
        dto1.setRetirement401k(BigDecimal.TEN);
        dto1.setPaymentMethod(PaymentMethod.DIRECT_DEPOSIT);
        dto1.setBankAccountNumber("test");
        dto1.setBankRoutingNumber("test");
        dto1.setTaxCode("test");
        dto1.setTaxExemptions(42);
        dto2.setCountryCode("test");
        dto2.setPayrollId("test");
        dto2.setEmployeeId("test");
        dto2.setEmployeeName("test");
        dto2.setEmployeeCode("test");
        dto2.setDepartment("test");
        dto2.setPosition("test");
        dto2.setBasicSalary(BigDecimal.TEN);
        dto2.setOvertimeHours(BigDecimal.TEN);
        dto2.setOvertimeRate(BigDecimal.TEN);
        dto2.setBonus(BigDecimal.TEN);
        dto2.setCommission(BigDecimal.TEN);
        dto2.setAllowances(BigDecimal.TEN);
        dto2.setHealthInsurance(BigDecimal.TEN);
        dto2.setDentalInsurance(BigDecimal.TEN);
        dto2.setRetirement401k(BigDecimal.TEN);
        dto2.setPaymentMethod(PaymentMethod.DIRECT_DEPOSIT);
        dto2.setBankAccountNumber("test");
        dto2.setBankRoutingNumber("test");
        dto2.setTaxCode("test");
        dto2.setTaxExemptions(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCountryCode(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PayrollEntryController.CreateEntryRequestDto dto = new PayrollEntryController.CreateEntryRequestDto();
        dto.setCountryCode("test");
        dto.setPayrollId("test");
        dto.setEmployeeId("test");
        dto.setEmployeeName("test");
        dto.setEmployeeCode("test");
        dto.setDepartment("test");
        dto.setPosition("test");
        dto.setBasicSalary(BigDecimal.TEN);
        dto.setOvertimeHours(BigDecimal.TEN);
        dto.setOvertimeRate(BigDecimal.TEN);
        dto.setBonus(BigDecimal.TEN);
        dto.setCommission(BigDecimal.TEN);
        dto.setAllowances(BigDecimal.TEN);
        dto.setHealthInsurance(BigDecimal.TEN);
        dto.setDentalInsurance(BigDecimal.TEN);
        dto.setRetirement401k(BigDecimal.TEN);
        dto.setPaymentMethod(PaymentMethod.DIRECT_DEPOSIT);
        dto.setBankAccountNumber("test");
        dto.setBankRoutingNumber("test");
        dto.setTaxCode("test");
        dto.setTaxExemptions(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PayrollEntryController.CreateEntryRequestDto dto = new PayrollEntryController.CreateEntryRequestDto();
        dto.setCountryCode("test");
        dto.setPayrollId("test");
        dto.setEmployeeId("test");
        dto.setEmployeeName("test");
        dto.setEmployeeCode("test");
        dto.setDepartment("test");
        dto.setPosition("test");
        dto.setBasicSalary(BigDecimal.TEN);
        dto.setOvertimeHours(BigDecimal.TEN);
        dto.setOvertimeRate(BigDecimal.TEN);
        dto.setBonus(BigDecimal.TEN);
        dto.setCommission(BigDecimal.TEN);
        dto.setAllowances(BigDecimal.TEN);
        dto.setHealthInsurance(BigDecimal.TEN);
        dto.setDentalInsurance(BigDecimal.TEN);
        dto.setRetirement401k(BigDecimal.TEN);
        dto.setPaymentMethod(PaymentMethod.DIRECT_DEPOSIT);
        dto.setBankAccountNumber("test");
        dto.setBankRoutingNumber("test");
        dto.setTaxCode("test");
        dto.setTaxExemptions(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}