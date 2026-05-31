package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.interfaces.rest.TaxCalculationController;
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
class TaxCalculationController_AddTaxRateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationController.AddTaxRateRequestDto dto = new TaxCalculationController.AddTaxRateRequestDto();
        dto.setTaxCode("val-taxCode");
        dto.setRate(BigDecimal.ONE);
        dto.setIsRecoverable(true);
        dto.setDescription("val-description");
        dto.setIsCompound(true);
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertTrue(dto.getIsRecoverable());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsCompound());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationController.AddTaxRateRequestDto dto1 = new TaxCalculationController.AddTaxRateRequestDto();
        TaxCalculationController.AddTaxRateRequestDto dto2 = new TaxCalculationController.AddTaxRateRequestDto();
        dto1.setTaxCode("test");
        dto1.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto1.setRate(BigDecimal.TEN);
        dto1.setIsRecoverable(true);
        dto1.setDescription("test");
        dto1.setIsCompound(true);
        dto2.setTaxCode("test");
        dto2.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto2.setRate(BigDecimal.TEN);
        dto2.setIsRecoverable(true);
        dto2.setDescription("test");
        dto2.setIsCompound(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTaxCode(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationController.AddTaxRateRequestDto dto = new TaxCalculationController.AddTaxRateRequestDto();
        dto.setTaxCode("test");
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setRate(BigDecimal.TEN);
        dto.setIsRecoverable(true);
        dto.setDescription("test");
        dto.setIsCompound(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationController.AddTaxRateRequestDto dto = new TaxCalculationController.AddTaxRateRequestDto();
        dto.setTaxCode("test");
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setRate(BigDecimal.TEN);
        dto.setIsRecoverable(true);
        dto.setDescription("test");
        dto.setIsCompound(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}