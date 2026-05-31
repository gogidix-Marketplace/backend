package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.domain.model.TaxRule;
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
class TaxRule_TaxBracketTest {

        @Test
    void testBuilder() {
        TaxRule.TaxBracket dto = TaxRule.TaxBracket.builder()
                        .min(BigDecimal.TEN)
            .max(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .priority(42)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getMin());
        assertEquals(BigDecimal.TEN, dto.getMax());
        assertEquals(BigDecimal.TEN, dto.getRate());
        assertEquals(42, dto.getPriority());
    }

    @Test
    void testSettersAndGetters() {
        TaxRule.TaxBracket dto = new TaxRule.TaxBracket();
        dto.setMin(BigDecimal.ONE);
        dto.setMax(BigDecimal.ONE);
        dto.setRate(BigDecimal.ONE);
        dto.setPriority(99);
        assertEquals(BigDecimal.ONE, dto.getMin());
        assertEquals(BigDecimal.ONE, dto.getMax());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertEquals(99, dto.getPriority());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRule.TaxBracket dto1 = TaxRule.TaxBracket.builder()
                        .min(BigDecimal.TEN)
            .max(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .priority(42)
            .build();
        TaxRule.TaxBracket dto2 = TaxRule.TaxBracket.builder()
                        .min(BigDecimal.TEN)
            .max(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .priority(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxRule.TaxBracket dto = TaxRule.TaxBracket.builder()
                        .min(BigDecimal.TEN)
            .max(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .priority(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}