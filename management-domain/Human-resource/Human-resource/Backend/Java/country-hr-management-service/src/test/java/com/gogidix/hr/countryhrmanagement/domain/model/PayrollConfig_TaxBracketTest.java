package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.domain.model.PayrollConfig;
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
class PayrollConfig_TaxBracketTest {

        @Test
    void testBuilder() {
        PayrollConfig.TaxBracket dto = PayrollConfig.TaxBracket.builder()
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
        PayrollConfig.TaxBracket dto = new PayrollConfig.TaxBracket();
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
        PayrollConfig.TaxBracket dto1 = PayrollConfig.TaxBracket.builder()
                        .bracketCode("test-bracketCode")
            .bracketName("test-bracketName")
            .minAmount(BigDecimal.TEN)
            .maxAmount(BigDecimal.TEN)
            .taxRate(BigDecimal.TEN)
            .fixedAmount(BigDecimal.TEN)
            .build();
        PayrollConfig.TaxBracket dto2 = PayrollConfig.TaxBracket.builder()
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
        PayrollConfig.TaxBracket dto = PayrollConfig.TaxBracket.builder()
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