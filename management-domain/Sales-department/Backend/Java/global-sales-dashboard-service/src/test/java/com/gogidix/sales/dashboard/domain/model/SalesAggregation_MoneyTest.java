package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.SalesAggregation;
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
class SalesAggregation_MoneyTest {

        @Test
    void testBuilder() {
        SalesAggregation.Money dto = SalesAggregation.Money.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testSettersAndGetters() {
        SalesAggregation.Money dto = new SalesAggregation.Money();
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        SalesAggregation.Money dto1 = SalesAggregation.Money.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        SalesAggregation.Money dto2 = SalesAggregation.Money.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SalesAggregation.Money dto = SalesAggregation.Money.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}