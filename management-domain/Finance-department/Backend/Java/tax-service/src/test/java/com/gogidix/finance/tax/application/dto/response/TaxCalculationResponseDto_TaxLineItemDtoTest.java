package com.gogidix.finance.tax.application.dto.response;

import com.gogidix.finance.tax.application.dto.response.TaxCalculationResponseDto;
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
class TaxCalculationResponseDto_TaxLineItemDtoTest {

        @Test
    void testBuilder() {
        TaxCalculationResponseDto.TaxLineItemDto dto = TaxCalculationResponseDto.TaxLineItemDto.builder()
                        .taxCode("test-taxCode")
            .taxType(null)
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
        TaxCalculationResponseDto.TaxLineItemDto dto = new TaxCalculationResponseDto.TaxLineItemDto();
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
        TaxCalculationResponseDto.TaxLineItemDto dto1 = TaxCalculationResponseDto.TaxLineItemDto.builder()
                        .taxCode("test-taxCode")
            .taxType(null)
            .rate(BigDecimal.TEN)
            .baseAmount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .isRecoverable(true)
            .recoverableAmount(BigDecimal.TEN)
            .description("test-description")
            .build();
        TaxCalculationResponseDto.TaxLineItemDto dto2 = TaxCalculationResponseDto.TaxLineItemDto.builder()
                        .taxCode("test-taxCode")
            .taxType(null)
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
        TaxCalculationResponseDto.TaxLineItemDto dto = TaxCalculationResponseDto.TaxLineItemDto.builder()
                        .taxCode("test-taxCode")
            .taxType(null)
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