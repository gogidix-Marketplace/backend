package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.MetricRollup;
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
class MetricRollup_MoneyTest {

        @Test
    void testBuilder() {
        MetricRollup.Money dto = MetricRollup.Money.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testSettersAndGetters() {
        MetricRollup.Money dto = new MetricRollup.Money();
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        MetricRollup.Money dto1 = MetricRollup.Money.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        MetricRollup.Money dto2 = MetricRollup.Money.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MetricRollup.Money dto = MetricRollup.Money.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}