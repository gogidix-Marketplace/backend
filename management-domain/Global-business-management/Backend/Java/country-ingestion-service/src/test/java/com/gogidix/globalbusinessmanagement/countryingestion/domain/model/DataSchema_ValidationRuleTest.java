package com.gogidix.globalbusinessmanagement.countryingestion.domain.model;

import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.DataSchema;
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
class DataSchema_ValidationRuleTest {

        @Test
    void testBuilder() {
        DataSchema.ValidationRule dto = DataSchema.ValidationRule.builder()
                        .ruleName("test-ruleName")
            .ruleType(DataSchema.ValidationRule.RuleType.REQUIRED)
            .errorMessage("test-errorMessage")
            .parameters(Collections.emptyMap())
            .enabled(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-ruleName", dto.getRuleName());
        assertEquals(DataSchema.ValidationRule.RuleType.REQUIRED, dto.getRuleType());
        assertEquals("test-errorMessage", dto.getErrorMessage());
        assertTrue(dto.isEnabled());
    }

    @Test
    void testSettersAndGetters() {
        DataSchema.ValidationRule dto = new DataSchema.ValidationRule();
        dto.setRuleName("val-ruleName");
        dto.setRuleType(DataSchema.ValidationRule.RuleType.REQUIRED);
        dto.setErrorMessage("val-errorMessage");
        dto.setEnabled(true);
        assertEquals("val-ruleName", dto.getRuleName());
        assertEquals(DataSchema.ValidationRule.RuleType.REQUIRED, dto.getRuleType());
        assertEquals("val-errorMessage", dto.getErrorMessage());
        assertTrue(dto.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        DataSchema.ValidationRule dto1 = DataSchema.ValidationRule.builder()
                        .ruleName("test-ruleName")
            .ruleType(DataSchema.ValidationRule.RuleType.REQUIRED)
            .errorMessage("test-errorMessage")
            .parameters(Collections.emptyMap())
            .enabled(true)
            .build();
        DataSchema.ValidationRule dto2 = DataSchema.ValidationRule.builder()
                        .ruleName("test-ruleName")
            .ruleType(DataSchema.ValidationRule.RuleType.REQUIRED)
            .errorMessage("test-errorMessage")
            .parameters(Collections.emptyMap())
            .enabled(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DataSchema.ValidationRule dto = DataSchema.ValidationRule.builder()
                        .ruleName("test-ruleName")
            .ruleType(DataSchema.ValidationRule.RuleType.REQUIRED)
            .errorMessage("test-errorMessage")
            .parameters(Collections.emptyMap())
            .enabled(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}