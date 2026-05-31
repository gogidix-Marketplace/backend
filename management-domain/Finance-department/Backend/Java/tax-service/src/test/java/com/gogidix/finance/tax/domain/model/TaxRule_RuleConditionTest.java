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
class TaxRule_RuleConditionTest {

        @Test
    void testBuilder() {
        TaxRule.RuleCondition dto = TaxRule.RuleCondition.builder()
                        .field("test-field")
            .operator("test-operator")
            .value("test-value")
            .valueType("test-valueType")
            .isRequired(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-field", dto.getField());
        assertEquals("test-operator", dto.getOperator());
        assertEquals("test-value", dto.getValue());
        assertEquals("test-valueType", dto.getValueType());
        assertTrue(dto.getIsRequired());
    }

    @Test
    void testSettersAndGetters() {
        TaxRule.RuleCondition dto = new TaxRule.RuleCondition();
        dto.setField("val-field");
        dto.setOperator("val-operator");
        dto.setValue("val-value");
        dto.setValueType("val-valueType");
        dto.setIsRequired(true);
        assertEquals("val-field", dto.getField());
        assertEquals("val-operator", dto.getOperator());
        assertEquals("val-value", dto.getValue());
        assertEquals("val-valueType", dto.getValueType());
        assertTrue(dto.getIsRequired());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRule.RuleCondition dto1 = TaxRule.RuleCondition.builder()
                        .field("test-field")
            .operator("test-operator")
            .value("test-value")
            .valueType("test-valueType")
            .isRequired(true)
            .build();
        TaxRule.RuleCondition dto2 = TaxRule.RuleCondition.builder()
                        .field("test-field")
            .operator("test-operator")
            .value("test-value")
            .valueType("test-valueType")
            .isRequired(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxRule.RuleCondition dto = TaxRule.RuleCondition.builder()
                        .field("test-field")
            .operator("test-operator")
            .value("test-value")
            .valueType("test-valueType")
            .isRequired(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}