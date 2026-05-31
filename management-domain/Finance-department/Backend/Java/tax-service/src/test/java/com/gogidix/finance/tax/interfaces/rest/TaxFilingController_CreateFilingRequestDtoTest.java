package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.domain.model.TaxFiling;
import com.gogidix.finance.tax.domain.model.TaxRate;
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
class TaxFilingController_CreateFilingRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingController.CreateFilingRequestDto dto = new TaxFilingController.CreateFilingRequestDto();
        dto.setCurrency("val-currency");
        dto.setDueDate(LocalDate.of(2025,6,1));
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingController.CreateFilingRequestDto dto1 = new TaxFilingController.CreateFilingRequestDto();
        TaxFilingController.CreateFilingRequestDto dto2 = new TaxFilingController.CreateFilingRequestDto();
        dto1.setFilingPeriod(null);
        dto1.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto1.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto1.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto1.setCurrency("test");
        dto1.setDueDate(LocalDate.of(2025,1,1));
        dto2.setFilingPeriod(null);
        dto2.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto2.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto2.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto2.setCurrency("test");
        dto2.setDueDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFilingType(TaxFiling.FilingType.QUARTERLY_RETURN);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingController.CreateFilingRequestDto dto = new TaxFilingController.CreateFilingRequestDto();
        dto.setFilingPeriod(null);
        dto.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setCurrency("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingController.CreateFilingRequestDto dto = new TaxFilingController.CreateFilingRequestDto();
        dto.setFilingPeriod(null);
        dto.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setCurrency("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}