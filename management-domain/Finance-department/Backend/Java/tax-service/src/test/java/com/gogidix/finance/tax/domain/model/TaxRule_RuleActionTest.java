package com.gogidix.finance.tax.domain.model;

import com.gogidix.finance.tax.domain.model.TaxRule;
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
class TaxRule_RuleActionTest {

        @Test
    void testBuilder() {
        TaxRule.RuleAction dto = TaxRule.RuleAction.builder()
                        .actionType("test-actionType")
            .target("test-target")
            .value("test-value")
            .parameters(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-actionType", dto.getActionType());
        assertEquals("test-target", dto.getTarget());
        assertEquals("test-value", dto.getValue());
    }

    @Test
    void testSettersAndGetters() {
        TaxRule.RuleAction dto = new TaxRule.RuleAction();
        dto.setActionType("val-actionType");
        dto.setTarget("val-target");
        dto.setValue("val-value");
        assertEquals("val-actionType", dto.getActionType());
        assertEquals("val-target", dto.getTarget());
        assertEquals("val-value", dto.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRule.RuleAction dto1 = TaxRule.RuleAction.builder()
                        .actionType("test-actionType")
            .target("test-target")
            .value("test-value")
            .parameters(Collections.emptyMap())
            .build();
        TaxRule.RuleAction dto2 = TaxRule.RuleAction.builder()
                        .actionType("test-actionType")
            .target("test-target")
            .value("test-value")
            .parameters(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxRule.RuleAction dto = TaxRule.RuleAction.builder()
                        .actionType("test-actionType")
            .target("test-target")
            .value("test-value")
            .parameters(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}