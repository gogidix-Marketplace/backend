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
class TaxFilingController_AutoGenerateFilingRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingController.AutoGenerateFilingRequestDto dto = new TaxFilingController.AutoGenerateFilingRequestDto();


    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingController.AutoGenerateFilingRequestDto dto1 = new TaxFilingController.AutoGenerateFilingRequestDto();
        TaxFilingController.AutoGenerateFilingRequestDto dto2 = new TaxFilingController.AutoGenerateFilingRequestDto();
        dto1.setFilingPeriod(null);
        dto1.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto1.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto1.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        dto2.setFilingPeriod(null);
        dto2.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto2.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto2.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setJurisdiction(TaxRate.Jurisdiction.US_STATE);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingController.AutoGenerateFilingRequestDto dto = new TaxFilingController.AutoGenerateFilingRequestDto();
        dto.setFilingPeriod(null);
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingController.AutoGenerateFilingRequestDto dto = new TaxFilingController.AutoGenerateFilingRequestDto();
        dto.setFilingPeriod(null);
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setFilingType(TaxFiling.FilingType.MONTHLY_RETURN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}