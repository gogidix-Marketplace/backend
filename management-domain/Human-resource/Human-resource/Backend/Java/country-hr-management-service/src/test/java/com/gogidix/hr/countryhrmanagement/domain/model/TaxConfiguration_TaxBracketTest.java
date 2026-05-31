package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.domain.model.TaxConfiguration;
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
class TaxConfiguration_TaxBracketTest {

        @Test
    void testBuilder() {
        TaxConfiguration.TaxBracket dto = TaxConfiguration.TaxBracket.builder()
                        .bracketCode("test-bracketCode")
            .bracketName("test-bracketName")
            .minAmount(BigDecimal.TEN)
            .maxAmount(BigDecimal.TEN)
            .taxRate(BigDecimal.TEN)
            .fixedAmount(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-bracketCode", dto.getBracketCode());
        assertEquals("test-bracketName", dto.getBracketName());
        assertEquals(BigDecimal.TEN, dto.getMinAmount());
        assertEquals(BigDecimal.TEN, dto.getMaxAmount());
        assertEquals(BigDecimal.TEN, dto.getTaxRate());
        assertEquals(BigDecimal.TEN, dto.getFixedAmount());
    }

    @Test
    void testSettersAndGetters() {
        TaxConfiguration.TaxBracket dto = new TaxConfiguration.TaxBracket();
        dto.setBracketCode("val-bracketCode");
        dto.setBracketName("val-bracketName");
        dto.setMinAmount(BigDecimal.ONE);
        dto.setMaxAmount(BigDecimal.ONE);
        dto.setTaxRate(BigDecimal.ONE);
        dto.setFixedAmount(BigDecimal.ONE);
        assertEquals("val-bracketCode", dto.getBracketCode());
        assertEquals("val-bracketName", dto.getBracketName());
        assertEquals(BigDecimal.ONE, dto.getMinAmount());
        assertEquals(BigDecimal.ONE, dto.getMaxAmount());
        assertEquals(BigDecimal.ONE, dto.getTaxRate());
        assertEquals(BigDecimal.ONE, dto.getFixedAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxConfiguration.TaxBracket dto1 = TaxConfiguration.TaxBracket.builder()
                        .bracketCode("test-bracketCode")
            .bracketName("test-bracketName")
            .minAmount(BigDecimal.TEN)
            .maxAmount(BigDecimal.TEN)
            .taxRate(BigDecimal.TEN)
            .fixedAmount(BigDecimal.TEN)
            .build();
        TaxConfiguration.TaxBracket dto2 = TaxConfiguration.TaxBracket.builder()
                        .bracketCode("test-bracketCode")
            .bracketName("test-bracketName")
            .minAmount(BigDecimal.TEN)
            .maxAmount(BigDecimal.TEN)
            .taxRate(BigDecimal.TEN)
            .fixedAmount(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxConfiguration.TaxBracket dto = TaxConfiguration.TaxBracket.builder()
                        .bracketCode("test-bracketCode")
            .bracketName("test-bracketName")
            .minAmount(BigDecimal.TEN)
            .maxAmount(BigDecimal.TEN)
            .taxRate(BigDecimal.TEN)
            .fixedAmount(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}