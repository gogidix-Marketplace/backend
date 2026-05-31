package com.gogidix.hr.payroll.application.dto.response;

import com.gogidix.hr.payroll.application.dto.response.PayrollEntryResponse;
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
class PayrollEntryResponseTest {

        @Test
    void testBuilder() {
        PayrollEntryResponse dto = PayrollEntryResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
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
            .overtimePay(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .grossPay(BigDecimal.TEN)
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .totalTax(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .otherDeductions(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .netPay(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .checkNumber("test-checkNumber")
            .paymentDate(LocalDate.of(2025,1,15))
            .paid(true)
            .currency("test-currency")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .deductionDetails(Collections.emptyList())
            .earningDetails(Collections.emptyList())
            .notes("test-notes")
            .isHold(true)
            .holdReason("test-holdReason")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
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
        assertEquals(BigDecimal.TEN, dto.getOvertimePay());
        assertEquals(BigDecimal.TEN, dto.getBonus());
        assertEquals(BigDecimal.TEN, dto.getCommission());
        assertEquals(BigDecimal.TEN, dto.getAllowances());
        assertEquals(BigDecimal.TEN, dto.getGrossPay());
        assertEquals(BigDecimal.TEN, dto.getFederalTax());
        assertEquals(BigDecimal.TEN, dto.getStateTax());
        assertEquals(BigDecimal.TEN, dto.getLocalTax());
        assertEquals(BigDecimal.TEN, dto.getSocialSecurityTax());
        assertEquals(BigDecimal.TEN, dto.getMedicareTax());
        assertEquals(BigDecimal.TEN, dto.getOtherTaxes());
        assertEquals(BigDecimal.TEN, dto.getTotalTax());
        assertEquals(BigDecimal.TEN, dto.getHealthInsurance());
        assertEquals(BigDecimal.TEN, dto.getDentalInsurance());
        assertEquals(BigDecimal.TEN, dto.getRetirement401k());
        assertEquals(BigDecimal.TEN, dto.getOtherDeductions());
        assertEquals(BigDecimal.TEN, dto.getTotalDeductions());
        assertEquals(BigDecimal.TEN, dto.getNetPay());
        assertEquals("test-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("test-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("test-checkNumber", dto.getCheckNumber());
        assertEquals(LocalDate.of(2025,1,15), dto.getPaymentDate());
        assertTrue(dto.getPaid());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals(42, dto.getTaxExemptions());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getIsHold());
        assertEquals("test-holdReason", dto.getHoldReason());
    }

    @Test
    void testSettersAndGetters() {
        PayrollEntryResponse dto = new PayrollEntryResponse();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
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
        dto.setOvertimePay(BigDecimal.ONE);
        dto.setBonus(BigDecimal.ONE);
        dto.setCommission(BigDecimal.ONE);
        dto.setAllowances(BigDecimal.ONE);
        dto.setGrossPay(BigDecimal.ONE);
        dto.setFederalTax(BigDecimal.ONE);
        dto.setStateTax(BigDecimal.ONE);
        dto.setLocalTax(BigDecimal.ONE);
        dto.setSocialSecurityTax(BigDecimal.ONE);
        dto.setMedicareTax(BigDecimal.ONE);
        dto.setOtherTaxes(BigDecimal.ONE);
        dto.setTotalTax(BigDecimal.ONE);
        dto.setHealthInsurance(BigDecimal.ONE);
        dto.setDentalInsurance(BigDecimal.ONE);
        dto.setRetirement401k(BigDecimal.ONE);
        dto.setOtherDeductions(BigDecimal.ONE);
        dto.setTotalDeductions(BigDecimal.ONE);
        dto.setNetPay(BigDecimal.ONE);
        dto.setBankAccountNumber("val-bankAccountNumber");
        dto.setBankRoutingNumber("val-bankRoutingNumber");
        dto.setCheckNumber("val-checkNumber");
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setPaid(true);
        dto.setCurrency("val-currency");
        dto.setTaxCode("val-taxCode");
        dto.setTaxExemptions(99);
        dto.setNotes("val-notes");
        dto.setIsHold(true);
        dto.setHoldReason("val-holdReason");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
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
        assertEquals(BigDecimal.ONE, dto.getOvertimePay());
        assertEquals(BigDecimal.ONE, dto.getBonus());
        assertEquals(BigDecimal.ONE, dto.getCommission());
        assertEquals(BigDecimal.ONE, dto.getAllowances());
        assertEquals(BigDecimal.ONE, dto.getGrossPay());
        assertEquals(BigDecimal.ONE, dto.getFederalTax());
        assertEquals(BigDecimal.ONE, dto.getStateTax());
        assertEquals(BigDecimal.ONE, dto.getLocalTax());
        assertEquals(BigDecimal.ONE, dto.getSocialSecurityTax());
        assertEquals(BigDecimal.ONE, dto.getMedicareTax());
        assertEquals(BigDecimal.ONE, dto.getOtherTaxes());
        assertEquals(BigDecimal.ONE, dto.getTotalTax());
        assertEquals(BigDecimal.ONE, dto.getHealthInsurance());
        assertEquals(BigDecimal.ONE, dto.getDentalInsurance());
        assertEquals(BigDecimal.ONE, dto.getRetirement401k());
        assertEquals(BigDecimal.ONE, dto.getOtherDeductions());
        assertEquals(BigDecimal.ONE, dto.getTotalDeductions());
        assertEquals(BigDecimal.ONE, dto.getNetPay());
        assertEquals("val-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("val-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("val-checkNumber", dto.getCheckNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertTrue(dto.getPaid());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(99, dto.getTaxExemptions());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getIsHold());
        assertEquals("val-holdReason", dto.getHoldReason());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryResponse dto1 = PayrollEntryResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
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
            .overtimePay(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .grossPay(BigDecimal.TEN)
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .totalTax(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .otherDeductions(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .netPay(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .checkNumber("test-checkNumber")
            .paymentDate(LocalDate.of(2025,1,15))
            .paid(true)
            .currency("test-currency")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .deductionDetails(Collections.emptyList())
            .earningDetails(Collections.emptyList())
            .notes("test-notes")
            .isHold(true)
            .holdReason("test-holdReason")
            .build();
        PayrollEntryResponse dto2 = PayrollEntryResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
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
            .overtimePay(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .grossPay(BigDecimal.TEN)
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .totalTax(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .otherDeductions(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .netPay(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .checkNumber("test-checkNumber")
            .paymentDate(LocalDate.of(2025,1,15))
            .paid(true)
            .currency("test-currency")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .deductionDetails(Collections.emptyList())
            .earningDetails(Collections.emptyList())
            .notes("test-notes")
            .isHold(true)
            .holdReason("test-holdReason")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollEntryResponse dto = PayrollEntryResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
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
            .overtimePay(BigDecimal.TEN)
            .bonus(BigDecimal.TEN)
            .commission(BigDecimal.TEN)
            .allowances(BigDecimal.TEN)
            .grossPay(BigDecimal.TEN)
            .federalTax(BigDecimal.TEN)
            .stateTax(BigDecimal.TEN)
            .localTax(BigDecimal.TEN)
            .socialSecurityTax(BigDecimal.TEN)
            .medicareTax(BigDecimal.TEN)
            .otherTaxes(BigDecimal.TEN)
            .totalTax(BigDecimal.TEN)
            .healthInsurance(BigDecimal.TEN)
            .dentalInsurance(BigDecimal.TEN)
            .retirement401k(BigDecimal.TEN)
            .otherDeductions(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .netPay(BigDecimal.TEN)
            .paymentMethod(PaymentMethod.DIRECT_DEPOSIT)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .checkNumber("test-checkNumber")
            .paymentDate(LocalDate.of(2025,1,15))
            .paid(true)
            .currency("test-currency")
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .deductionDetails(Collections.emptyList())
            .earningDetails(Collections.emptyList())
            .notes("test-notes")
            .isHold(true)
            .holdReason("test-holdReason")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}