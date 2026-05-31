package com.gogidix.hr.payroll.application.dto.request;

import com.gogidix.hr.payroll.application.dto.request.UpdatePayrollEntryRequest;
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
class UpdatePayrollEntryRequestTest {

        @Test
    void testBuilder() {
        UpdatePayrollEntryRequest dto = UpdatePayrollEntryRequest.builder()
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
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .notes("test-notes")
            .build();
        assertNotNull(dto);
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
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals(42, dto.getTaxExemptions());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        UpdatePayrollEntryRequest dto = new UpdatePayrollEntryRequest();
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
        dto.setTaxCode("val-taxCode");
        dto.setTaxExemptions(99);
        dto.setNotes("val-notes");
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
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(99, dto.getTaxExemptions());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdatePayrollEntryRequest dto1 = UpdatePayrollEntryRequest.builder()
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
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .notes("test-notes")
            .build();
        UpdatePayrollEntryRequest dto2 = UpdatePayrollEntryRequest.builder()
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
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdatePayrollEntryRequest dto = UpdatePayrollEntryRequest.builder()
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
            .taxCode("test-taxCode")
            .taxExemptions(42)
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}