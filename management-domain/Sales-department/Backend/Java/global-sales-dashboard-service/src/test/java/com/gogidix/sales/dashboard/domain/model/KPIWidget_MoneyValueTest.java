package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.KPIWidget;
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
class KPIWidget_MoneyValueTest {

        @Test
    void testBuilder() {
        KPIWidget.MoneyValue dto = KPIWidget.MoneyValue.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .formatted("test-formatted")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-formatted", dto.getFormatted());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.MoneyValue dto = new KPIWidget.MoneyValue();
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setFormatted("val-formatted");
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-formatted", dto.getFormatted());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.MoneyValue dto1 = KPIWidget.MoneyValue.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .formatted("test-formatted")
            .build();
        KPIWidget.MoneyValue dto2 = KPIWidget.MoneyValue.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .formatted("test-formatted")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.MoneyValue dto = KPIWidget.MoneyValue.builder()
                        .amount(BigDecimal.TEN)
            .currency("test-currency")
            .formatted("test-formatted")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}