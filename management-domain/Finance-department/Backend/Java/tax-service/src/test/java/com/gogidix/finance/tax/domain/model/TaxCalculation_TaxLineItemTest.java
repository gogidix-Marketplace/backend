package com.gogidix.finance.tax.domain.model;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
import com.gogidix.finance.tax.domain.model.TaxRate;
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
class TaxCalculation_TaxLineItemTest {

        @Test
    void testBuilder() {
        TaxCalculation.TaxLineItem dto = TaxCalculation.TaxLineItem.builder()
                        .taxCode("test-taxCode")
            .taxType(TaxRate.TaxType.SALES_TAX)
            .rate(BigDecimal.TEN)
            .baseAmount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .isRecoverable(true)
            .recoverableAmount(BigDecimal.TEN)
            .description("test-description")
            .build();
        assertNotNull(dto);
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.TEN, dto.getRate());
        assertEquals(BigDecimal.TEN, dto.getBaseAmount());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertTrue(dto.getIsRecoverable());
        assertEquals(BigDecimal.TEN, dto.getRecoverableAmount());
        assertEquals("test-description", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        TaxCalculation.TaxLineItem dto = new TaxCalculation.TaxLineItem();
        dto.setTaxCode("val-taxCode");
        dto.setRate(BigDecimal.ONE);
        dto.setBaseAmount(BigDecimal.ONE);
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setIsRecoverable(true);
        dto.setRecoverableAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertEquals(BigDecimal.ONE, dto.getBaseAmount());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertTrue(dto.getIsRecoverable());
        assertEquals(BigDecimal.ONE, dto.getRecoverableAmount());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculation.TaxLineItem dto1 = TaxCalculation.TaxLineItem.builder()
                        .taxCode("test-taxCode")
            .taxType(TaxRate.TaxType.SALES_TAX)
            .rate(BigDecimal.TEN)
            .baseAmount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .isRecoverable(true)
            .recoverableAmount(BigDecimal.TEN)
            .description("test-description")
            .build();
        TaxCalculation.TaxLineItem dto2 = TaxCalculation.TaxLineItem.builder()
                        .taxCode("test-taxCode")
            .taxType(TaxRate.TaxType.SALES_TAX)
            .rate(BigDecimal.TEN)
            .baseAmount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .isRecoverable(true)
            .recoverableAmount(BigDecimal.TEN)
            .description("test-description")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxCalculation.TaxLineItem dto = TaxCalculation.TaxLineItem.builder()
                        .taxCode("test-taxCode")
            .taxType(TaxRate.TaxType.SALES_TAX)
            .rate(BigDecimal.TEN)
            .baseAmount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .isRecoverable(true)
            .recoverableAmount(BigDecimal.TEN)
            .description("test-description")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}