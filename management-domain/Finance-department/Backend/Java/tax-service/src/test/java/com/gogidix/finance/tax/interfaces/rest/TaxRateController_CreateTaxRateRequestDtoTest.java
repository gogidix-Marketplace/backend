package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.interfaces.rest.TaxRateController;
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
class TaxRateController_CreateTaxRateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxRateController.CreateTaxRateRequestDto dto = new TaxRateController.CreateTaxRateRequestDto();
        dto.setTaxCode("val-taxCode");
        dto.setRatePercentage(BigDecimal.ONE);
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setExpiryDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setIsCompound(true);
        dto.setIsRecoverable(true);
        dto.setRecoveryRate(BigDecimal.ONE);
        dto.setMinThreshold(BigDecimal.ONE);
        dto.setMaxThreshold(BigDecimal.ONE);
        dto.setNotes("val-notes");
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getRatePercentage());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpiryDate());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsCompound());
        assertTrue(dto.getIsRecoverable());
        assertEquals(BigDecimal.ONE, dto.getRecoveryRate());
        assertEquals(BigDecimal.ONE, dto.getMinThreshold());
        assertEquals(BigDecimal.ONE, dto.getMaxThreshold());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRateController.CreateTaxRateRequestDto dto1 = new TaxRateController.CreateTaxRateRequestDto();
        TaxRateController.CreateTaxRateRequestDto dto2 = new TaxRateController.CreateTaxRateRequestDto();
        dto1.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto1.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto1.setTaxCode("test");
        dto1.setRatePercentage(BigDecimal.TEN);
        dto1.setEffectiveDate(LocalDate.of(2025,1,1));
        dto1.setExpiryDate(LocalDate.of(2025,1,1));
        dto1.setDescription("test");
        dto1.setIsCompound(true);
        dto1.setIsRecoverable(true);
        dto1.setRecoveryRate(BigDecimal.TEN);
        dto1.setMinThreshold(BigDecimal.TEN);
        dto1.setMaxThreshold(BigDecimal.TEN);
        dto1.setNotes("test");
        dto2.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto2.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto2.setTaxCode("test");
        dto2.setRatePercentage(BigDecimal.TEN);
        dto2.setEffectiveDate(LocalDate.of(2025,1,1));
        dto2.setExpiryDate(LocalDate.of(2025,1,1));
        dto2.setDescription("test");
        dto2.setIsCompound(true);
        dto2.setIsRecoverable(true);
        dto2.setRecoveryRate(BigDecimal.TEN);
        dto2.setMinThreshold(BigDecimal.TEN);
        dto2.setMaxThreshold(BigDecimal.TEN);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setJurisdiction(TaxRate.Jurisdiction.US_STATE);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxRateController.CreateTaxRateRequestDto dto = new TaxRateController.CreateTaxRateRequestDto();
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setTaxCode("test");
        dto.setRatePercentage(BigDecimal.TEN);
        dto.setEffectiveDate(LocalDate.of(2025,1,1));
        dto.setExpiryDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setIsCompound(true);
        dto.setIsRecoverable(true);
        dto.setRecoveryRate(BigDecimal.TEN);
        dto.setMinThreshold(BigDecimal.TEN);
        dto.setMaxThreshold(BigDecimal.TEN);
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxRateController.CreateTaxRateRequestDto dto = new TaxRateController.CreateTaxRateRequestDto();
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setTaxCode("test");
        dto.setRatePercentage(BigDecimal.TEN);
        dto.setEffectiveDate(LocalDate.of(2025,1,1));
        dto.setExpiryDate(LocalDate.of(2025,1,1));
        dto.setDescription("test");
        dto.setIsCompound(true);
        dto.setIsRecoverable(true);
        dto.setRecoveryRate(BigDecimal.TEN);
        dto.setMinThreshold(BigDecimal.TEN);
        dto.setMaxThreshold(BigDecimal.TEN);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}