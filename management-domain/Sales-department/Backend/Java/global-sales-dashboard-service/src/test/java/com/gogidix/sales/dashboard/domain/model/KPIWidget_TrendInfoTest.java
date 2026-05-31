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
class KPIWidget_TrendInfoTest {

        @Test
    void testBuilder() {
        KPIWidget.TrendInfo dto = KPIWidget.TrendInfo.builder()
                        .direction("test-direction")
            .value(BigDecimal.TEN)
            .percentage("test-percentage")
            .label("test-label")
            .isPositive(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-direction", dto.getDirection());
        assertEquals(BigDecimal.TEN, dto.getValue());
        assertEquals("test-percentage", dto.getPercentage());
        assertEquals("test-label", dto.getLabel());
        assertTrue(dto.getIsPositive());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.TrendInfo dto = new KPIWidget.TrendInfo();
        dto.setDirection("val-direction");
        dto.setValue(BigDecimal.ONE);
        dto.setPercentage("val-percentage");
        dto.setLabel("val-label");
        dto.setIsPositive(true);
        assertEquals("val-direction", dto.getDirection());
        assertEquals(BigDecimal.ONE, dto.getValue());
        assertEquals("val-percentage", dto.getPercentage());
        assertEquals("val-label", dto.getLabel());
        assertTrue(dto.getIsPositive());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.TrendInfo dto1 = KPIWidget.TrendInfo.builder()
                        .direction("test-direction")
            .value(BigDecimal.TEN)
            .percentage("test-percentage")
            .label("test-label")
            .isPositive(true)
            .build();
        KPIWidget.TrendInfo dto2 = KPIWidget.TrendInfo.builder()
                        .direction("test-direction")
            .value(BigDecimal.TEN)
            .percentage("test-percentage")
            .label("test-label")
            .isPositive(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.TrendInfo dto = KPIWidget.TrendInfo.builder()
                        .direction("test-direction")
            .value(BigDecimal.TEN)
            .percentage("test-percentage")
            .label("test-label")
            .isPositive(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}