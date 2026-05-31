package com.gogidix.hr.payroll.application.dto.request;

import com.gogidix.hr.payroll.application.dto.request.CreatePayrollEntryRequest;
import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
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
class CreatePayrollEntryRequestTest {

        @Test
    void testBuilder() {
        CreatePayrollEntryRequest dto = CreatePayrollEntryRequest.builder()
                        .countryCode("test-countryCode")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .payPeriodStart(LocalDate.of(2025,1,15))
            .payPeriodEnd(LocalDate.of(2025,1,15))
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .otherDeductions(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .currency("test-currency")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .deductionDetails(Collections.emptyList())
            .earningDetails(Collections.emptyList())
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-employeeName", dto.getEmployeeName());
        assertEquals("test-employeeCode", dto.getEmployeeCode());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-position", dto.getPosition());
        assertEquals(LocalDate.of(2025,1,15), dto.getPayPeriodStart());
        assertEquals(LocalDate.of(2025,1,15), dto.getPayPeriodEnd());
        assertEquals(BigDecimal.TEN, dto.getBasicSalary());
        assertEquals(BigDecimal.TEN, dto.getOvertimeHours());
        assertEquals(BigDecimal.TEN, dto.getOvertimeRate());
        assertEquals(BigDecimal.TEN, dto.getBonus());
        assertEquals(BigDecimal.TEN, dto.getCommission());
        assertEquals(BigDecimal.TEN, dto.getAllowances());
        assertEquals(BigDecimal.TEN, dto.getFederalTax());
        assertEquals(BigDecimal.TEN, dto.getStateTax());
        assertEquals(BigDecimal.TEN, dto.getLocalTax());
        assertEquals(BigDecimal.TEN, dto.getSocialSecurityTax());
        assertEquals(BigDecimal.TEN, dto.getMedicareTax());
        assertEquals(BigDecimal.TEN, dto.getOtherTaxes());
        assertEquals(BigDecimal.TEN, dto.getHealthInsurance());
        assertEquals(BigDecimal.TEN, dto.getDentalInsurance());
        assertEquals(BigDecimal.TEN, dto.getRetirement401k());
        assertEquals(BigDecimal.TEN, dto.getOtherDeductions());
        assertEquals("test-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("test-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals(42, dto.getTaxExemptions());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        CreatePayrollEntryRequest dto = new CreatePayrollEntryRequest();
        dto.setCountryCode("val-countryCode");
        dto.setPayrollId("val-payrollId");
        dto.setEmployeeId("val-employeeId");
        dto.setEmployeeName("val-employeeName");
        dto.setEmployeeCode("val-employeeCode");
        dto.setDepartment("val-department");
        dto.setPosition("val-position");
        dto.setPayPeriodStart(LocalDate.of(2025,6,1));
        dto.setPayPeriodEnd(LocalDate.of(2025,6,1));
        dto.setBasicSalary(BigDecimal.ONE);
        dto.setOvertimeHours(BigDecimal.ONE);
        dto.setOvertimeRate(BigDecimal.ONE);
        dto.setBonus(BigDecimal.ONE);
        dto.setCommission(BigDecimal.ONE);
        dto.setAllowances(BigDecimal.ONE);
        dto.setFederalTax(BigDecimal.ONE);
        dto.setStateTax(BigDecimal.ONE);
        dto.setLocalTax(BigDecimal.ONE);
        dto.setSocialSecurityTax(BigDecimal.ONE);
        dto.setMedicareTax(BigDecimal.ONE);
        dto.setOtherTaxes(BigDecimal.ONE);
        dto.setHealthInsurance(BigDecimal.ONE);
        dto.setDentalInsurance(BigDecimal.ONE);
        dto.setRetirement401k(BigDecimal.ONE);
        dto.setOtherDeductions(BigDecimal.ONE);
        dto.setBankAccountNumber("val-bankAccountNumber");
        dto.setBankRoutingNumber("val-bankRoutingNumber");
        dto.setCurrency("val-currency");
        dto.setTaxCode("val-taxCode");
        dto.setTaxExemptions(99);
        dto.setNotes("val-notes");
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-employeeName", dto.getEmployeeName());
        assertEquals("val-employeeCode", dto.getEmployeeCode());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-position", dto.getPosition());
        assertEquals(LocalDate.of(2025,6,1), dto.getPayPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPayPeriodEnd());
        assertEquals(BigDecimal.ONE, dto.getBasicSalary());
        assertEquals(BigDecimal.ONE, dto.getOvertimeHours());
        assertEquals(BigDecimal.ONE, dto.getOvertimeRate());
        assertEquals(BigDecimal.ONE, dto.getBonus());
        assertEquals(BigDecimal.ONE, dto.getCommission());
        assertEquals(BigDecimal.ONE, dto.getAllowances());
        assertEquals(BigDecimal.ONE, dto.getFederalTax());
        assertEquals(BigDecimal.ONE, dto.getStateTax());
        assertEquals(BigDecimal.ONE, dto.getLocalTax());
        assertEquals(BigDecimal.ONE, dto.getSocialSecurityTax());
        assertEquals(BigDecimal.ONE, dto.getMedicareTax());
        assertEquals(BigDecimal.ONE, dto.getOtherTaxes());
        assertEquals(BigDecimal.ONE, dto.getHealthInsurance());
        assertEquals(BigDecimal.ONE, dto.getDentalInsurance());
        assertEquals(BigDecimal.ONE, dto.getRetirement401k());
        assertEquals(BigDecimal.ONE, dto.getOtherDeductions());
        assertEquals("val-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("val-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(99, dto.getTaxExemptions());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        CreatePayrollEntryRequest dto1 = CreatePayrollEntryRequest.builder()
                        .countryCode("test-countryCode")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .payPeriodStart(LocalDate.of(2025,1,15))
            .payPeriodEnd(LocalDate.of(2025,1,15))
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .otherDeductions(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .currency("test-currency")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .deductionDetails(Collections.emptyList())
            .earningDetails(Collections.emptyList())
            .notes("test-notes")
            .build();
        CreatePayrollEntryRequest dto2 = CreatePayrollEntryRequest.builder()
                        .countryCode("test-countryCode")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .payPeriodStart(LocalDate.of(2025,1,15))
            .payPeriodEnd(LocalDate.of(2025,1,15))
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .otherDeductions(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .currency("test-currency")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .deductionDetails(Collections.emptyList())
            .earningDetails(Collections.emptyList())
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreatePayrollEntryRequest dto = CreatePayrollEntryRequest.builder()
                        .countryCode("test-countryCode")
            .payrollId("test-payrollId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .payPeriodStart(LocalDate.of(2025,1,15))
            .payPeriodEnd(LocalDate.of(2025,1,15))
            .basicSalary(BigDecimal.TEN)
            .overtimeHours(BigDecimal.TEN)
            .overtimeRate(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .otherDeductions(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .currency("test-currency")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .deductionDetails(Collections.emptyList())
            .earningDetails(Collections.emptyList())
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}