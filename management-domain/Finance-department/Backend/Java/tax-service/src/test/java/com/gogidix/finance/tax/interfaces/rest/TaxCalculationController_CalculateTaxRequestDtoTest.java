package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
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
class TaxCalculationController_CalculateTaxRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationController.CalculateTaxRequestDto dto = new TaxCalculationController.CalculateTaxRequestDto();
        dto.setTransactionId("val-transactionId");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setCurrency("val-currency");
        dto.setAmount(BigDecimal.ONE);
        dto.setCategory("val-category");
        dto.setEntityCode("val-entityCode");
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-entityCode", dto.getEntityCode());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationController.CalculateTaxRequestDto dto1 = new TaxCalculationController.CalculateTaxRequestDto();
        TaxCalculationController.CalculateTaxRequestDto dto2 = new TaxCalculationController.CalculateTaxRequestDto();
        dto1.setTransactionId("test");
        dto1.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto1.setTransactionDate(LocalDate.of(2025,1,1));
        dto1.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto1.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto1.setCurrency("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setCategory("test");
        dto1.setEntityCode("test");
        dto1.setAdditionalContext(Collections.emptyMap());
        dto2.setTransactionId("test");
        dto2.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto2.setTransactionDate(LocalDate.of(2025,1,1));
        dto2.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto2.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto2.setCurrency("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setCategory("test");
        dto2.setEntityCode("test");
        dto2.setAdditionalContext(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTransactionId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationController.CalculateTaxRequestDto dto = new TaxCalculationController.CalculateTaxRequestDto();
        dto.setTransactionId("test");
        dto.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setCurrency("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setCategory("test");
        dto.setEntityCode("test");
        dto.setAdditionalContext(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationController.CalculateTaxRequestDto dto = new TaxCalculationController.CalculateTaxRequestDto();
        dto.setTransactionId("test");
        dto.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setTaxType(TaxRate.TaxType.SALES_TAX);
        dto.setCurrency("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setCategory("test");
        dto.setEntityCode("test");
        dto.setAdditionalContext(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}