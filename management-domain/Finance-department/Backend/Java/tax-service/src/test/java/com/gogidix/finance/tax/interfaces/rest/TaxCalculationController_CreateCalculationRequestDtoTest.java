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
class TaxCalculationController_CreateCalculationRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationController.CreateCalculationRequestDto dto = new TaxCalculationController.CreateCalculationRequestDto();
        dto.setTransactionId("val-transactionId");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setCurrency("val-currency");
        dto.setBaseAmount(BigDecimal.ONE);
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getBaseAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationController.CreateCalculationRequestDto dto1 = new TaxCalculationController.CreateCalculationRequestDto();
        TaxCalculationController.CreateCalculationRequestDto dto2 = new TaxCalculationController.CreateCalculationRequestDto();
        dto1.setTransactionId("test");
        dto1.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto1.setTransactionDate(LocalDate.of(2025,1,1));
        dto1.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto1.setCurrency("test");
        dto1.setBaseAmount(BigDecimal.TEN);
        dto1.setContext(Collections.emptyMap());
        dto2.setTransactionId("test");
        dto2.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto2.setTransactionDate(LocalDate.of(2025,1,1));
        dto2.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto2.setCurrency("test");
        dto2.setBaseAmount(BigDecimal.TEN);
        dto2.setContext(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTransactionId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationController.CreateCalculationRequestDto dto = new TaxCalculationController.CreateCalculationRequestDto();
        dto.setTransactionId("test");
        dto.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setCurrency("test");
        dto.setBaseAmount(BigDecimal.TEN);
        dto.setContext(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationController.CreateCalculationRequestDto dto = new TaxCalculationController.CreateCalculationRequestDto();
        dto.setTransactionId("test");
        dto.setTransactionType(TaxCalculation.TransactionType.SALES);
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction(TaxRate.Jurisdiction.US_FEDERAL);
        dto.setCurrency("test");
        dto.setBaseAmount(BigDecimal.TEN);
        dto.setContext(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}