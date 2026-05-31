package com.gogidix.hr.payroll.interfaces.rest;

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
class PayrollEntryController_UpdateTaxRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        PayrollEntryController.UpdateTaxRequestDto dto = new PayrollEntryController.UpdateTaxRequestDto();
        dto.setFederalTax(BigDecimal.ONE);
        dto.setStateTax(BigDecimal.ONE);
        dto.setLocalTax(BigDecimal.ONE);
        dto.setSocialSecurityTax(BigDecimal.ONE);
        dto.setMedicareTax(BigDecimal.ONE);
        dto.setOtherTaxes(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getFederalTax());
        assertEquals(BigDecimal.ONE, dto.getStateTax());
        assertEquals(BigDecimal.ONE, dto.getLocalTax());
        assertEquals(BigDecimal.ONE, dto.getSocialSecurityTax());
        assertEquals(BigDecimal.ONE, dto.getMedicareTax());
        assertEquals(BigDecimal.ONE, dto.getOtherTaxes());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryController.UpdateTaxRequestDto dto1 = new PayrollEntryController.UpdateTaxRequestDto();
        PayrollEntryController.UpdateTaxRequestDto dto2 = new PayrollEntryController.UpdateTaxRequestDto();
        dto1.setFederalTax(BigDecimal.TEN);
        dto1.setStateTax(BigDecimal.TEN);
        dto1.setLocalTax(BigDecimal.TEN);
        dto1.setSocialSecurityTax(BigDecimal.TEN);
        dto1.setMedicareTax(BigDecimal.TEN);
        dto1.setOtherTaxes(BigDecimal.TEN);
        dto2.setFederalTax(BigDecimal.TEN);
        dto2.setStateTax(BigDecimal.TEN);
        dto2.setLocalTax(BigDecimal.TEN);
        dto2.setSocialSecurityTax(BigDecimal.TEN);
        dto2.setMedicareTax(BigDecimal.TEN);
        dto2.setOtherTaxes(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFederalTax(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PayrollEntryController.UpdateTaxRequestDto dto = new PayrollEntryController.UpdateTaxRequestDto();
        dto.setFederalTax(BigDecimal.TEN);
        dto.setStateTax(BigDecimal.TEN);
        dto.setLocalTax(BigDecimal.TEN);
        dto.setSocialSecurityTax(BigDecimal.TEN);
        dto.setMedicareTax(BigDecimal.TEN);
        dto.setOtherTaxes(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PayrollEntryController.UpdateTaxRequestDto dto = new PayrollEntryController.UpdateTaxRequestDto();
        dto.setFederalTax(BigDecimal.TEN);
        dto.setStateTax(BigDecimal.TEN);
        dto.setLocalTax(BigDecimal.TEN);
        dto.setSocialSecurityTax(BigDecimal.TEN);
        dto.setMedicareTax(BigDecimal.TEN);
        dto.setOtherTaxes(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}