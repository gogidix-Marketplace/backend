package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.interfaces.rest.TaxFilingController;
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
class TaxFilingController_UpdateFiguresRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingController.UpdateFiguresRequestDto dto = new TaxFilingController.UpdateFiguresRequestDto();
        dto.setGrossSales(BigDecimal.ONE);
        dto.setTaxableSales(BigDecimal.ONE);
        dto.setExemptSales(BigDecimal.ONE);
        dto.setTotalTaxCollected(BigDecimal.ONE);
        dto.setTotalTaxPaid(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getGrossSales());
        assertEquals(BigDecimal.ONE, dto.getTaxableSales());
        assertEquals(BigDecimal.ONE, dto.getExemptSales());
        assertEquals(BigDecimal.ONE, dto.getTotalTaxCollected());
        assertEquals(BigDecimal.ONE, dto.getTotalTaxPaid());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingController.UpdateFiguresRequestDto dto1 = new TaxFilingController.UpdateFiguresRequestDto();
        TaxFilingController.UpdateFiguresRequestDto dto2 = new TaxFilingController.UpdateFiguresRequestDto();
        dto1.setGrossSales(BigDecimal.TEN);
        dto1.setTaxableSales(BigDecimal.TEN);
        dto1.setExemptSales(BigDecimal.TEN);
        dto1.setTotalTaxCollected(BigDecimal.TEN);
        dto1.setTotalTaxPaid(BigDecimal.TEN);
        dto2.setGrossSales(BigDecimal.TEN);
        dto2.setTaxableSales(BigDecimal.TEN);
        dto2.setExemptSales(BigDecimal.TEN);
        dto2.setTotalTaxCollected(BigDecimal.TEN);
        dto2.setTotalTaxPaid(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setGrossSales(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingController.UpdateFiguresRequestDto dto = new TaxFilingController.UpdateFiguresRequestDto();
        dto.setGrossSales(BigDecimal.TEN);
        dto.setTaxableSales(BigDecimal.TEN);
        dto.setExemptSales(BigDecimal.TEN);
        dto.setTotalTaxCollected(BigDecimal.TEN);
        dto.setTotalTaxPaid(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingController.UpdateFiguresRequestDto dto = new TaxFilingController.UpdateFiguresRequestDto();
        dto.setGrossSales(BigDecimal.TEN);
        dto.setTaxableSales(BigDecimal.TEN);
        dto.setExemptSales(BigDecimal.TEN);
        dto.setTotalTaxCollected(BigDecimal.TEN);
        dto.setTotalTaxPaid(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}