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
class ConsolidationReport_CashFlowLineItemTest {

        @Test
    void testBuilder() {
        ConsolidationReport.CashFlowLineItem dto = ConsolidationReport.CashFlowLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .build();
        assertNotNull(dto);
        assertEquals("test-lineCode", dto.getLineCode());
        assertEquals("test-lineName", dto.getLineName());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-description", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.CashFlowLineItem dto = new ConsolidationReport.CashFlowLineItem();
        dto.setLineCode("val-lineCode");
        dto.setLineName("val-lineName");
        dto.setAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        assertEquals("val-lineCode", dto.getLineCode());
        assertEquals("val-lineName", dto.getLineName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.CashFlowLineItem dto1 = ConsolidationReport.CashFlowLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .build();
        ConsolidationReport.CashFlowLineItem dto2 = ConsolidationReport.CashFlowLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.CashFlowLineItem dto = ConsolidationReport.CashFlowLineItem.builder()
                        .lineCode("test-lineCode")
            .lineName("test-lineName")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}