package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidationReport;
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
class ConsolidationReport_EquityLineItemTest {

        @Test
    void testBuilder() {
        ConsolidationReport.EquityLineItem dto = ConsolidationReport.EquityLineItem.builder()
                        .componentType("test-componentType")
            .componentName("test-componentName")
            .amount(BigDecimal.TEN)
            .movements(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-componentType", dto.getComponentType());
        assertEquals("test-componentName", dto.getComponentName());
        assertEquals(BigDecimal.TEN, dto.getAmount());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.EquityLineItem dto = new ConsolidationReport.EquityLineItem();
        dto.setComponentType("val-componentType");
        dto.setComponentName("val-componentName");
        dto.setAmount(BigDecimal.ONE);
        assertEquals("val-componentType", dto.getComponentType());
        assertEquals("val-componentName", dto.getComponentName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.EquityLineItem dto1 = ConsolidationReport.EquityLineItem.builder()
                        .componentType("test-componentType")
            .componentName("test-componentName")
            .amount(BigDecimal.TEN)
            .movements(Collections.emptyList())
            .build();
        ConsolidationReport.EquityLineItem dto2 = ConsolidationReport.EquityLineItem.builder()
                        .componentType("test-componentType")
            .componentName("test-componentName")
            .amount(BigDecimal.TEN)
            .movements(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.EquityLineItem dto = ConsolidationReport.EquityLineItem.builder()
                        .componentType("test-componentType")
            .componentName("test-componentName")
            .amount(BigDecimal.TEN)
            .movements(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}