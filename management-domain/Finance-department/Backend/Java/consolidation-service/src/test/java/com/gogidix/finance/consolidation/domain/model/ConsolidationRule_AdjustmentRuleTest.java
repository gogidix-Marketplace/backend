package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidationRule;
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
class ConsolidationRule_AdjustmentRuleTest {

        @Test
    void testBuilder() {
        ConsolidationRule.AdjustmentRule dto = ConsolidationRule.AdjustmentRule.builder()
                        .adjustmentId("test-adjustmentId")
            .accountCode("test-accountCode")
            .type(null)
            .percentage(BigDecimal.TEN)
            .formula("test-formula")
            .description("test-description")
            .build();
        assertNotNull(dto);
        assertEquals("test-adjustmentId", dto.getAdjustmentId());
        assertEquals("test-accountCode", dto.getAccountCode());
        assertEquals(BigDecimal.TEN, dto.getPercentage());
        assertEquals("test-formula", dto.getFormula());
        assertEquals("test-description", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationRule.AdjustmentRule dto = new ConsolidationRule.AdjustmentRule();
        dto.setAdjustmentId("val-adjustmentId");
        dto.setAccountCode("val-accountCode");
        dto.setPercentage(BigDecimal.ONE);
        dto.setFormula("val-formula");
        dto.setDescription("val-description");
        assertEquals("val-adjustmentId", dto.getAdjustmentId());
        assertEquals("val-accountCode", dto.getAccountCode());
        assertEquals(BigDecimal.ONE, dto.getPercentage());
        assertEquals("val-formula", dto.getFormula());
        assertEquals("val-description", dto.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationRule.AdjustmentRule dto1 = ConsolidationRule.AdjustmentRule.builder()
                        .adjustmentId("test-adjustmentId")
            .accountCode("test-accountCode")
            .type(null)
            .percentage(BigDecimal.TEN)
            .formula("test-formula")
            .description("test-description")
            .build();
        ConsolidationRule.AdjustmentRule dto2 = ConsolidationRule.AdjustmentRule.builder()
                        .adjustmentId("test-adjustmentId")
            .accountCode("test-accountCode")
            .type(null)
            .percentage(BigDecimal.TEN)
            .formula("test-formula")
            .description("test-description")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationRule.AdjustmentRule dto = ConsolidationRule.AdjustmentRule.builder()
                        .adjustmentId("test-adjustmentId")
            .accountCode("test-accountCode")
            .type(null)
            .percentage(BigDecimal.TEN)
            .formula("test-formula")
            .description("test-description")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}