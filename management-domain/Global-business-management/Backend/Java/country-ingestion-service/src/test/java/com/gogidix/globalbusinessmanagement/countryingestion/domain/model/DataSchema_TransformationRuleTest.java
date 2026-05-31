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
class DataSchema_TransformationRuleTest {

        @Test
    void testBuilder() {
        DataSchema.TransformationRule dto = DataSchema.TransformationRule.builder()
                        .ruleName("test-ruleName")
            .type(DataSchema.TransformationRule.TransformationType.TRIM)
            .parameters(Collections.emptyMap())
            .expression("test-expression")
            .enabled(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-ruleName", dto.getRuleName());
        assertEquals(DataSchema.TransformationRule.TransformationType.TRIM, dto.getType());
        assertEquals("test-expression", dto.getExpression());
        assertTrue(dto.isEnabled());
    }

    @Test
    void testSettersAndGetters() {
        DataSchema.TransformationRule dto = new DataSchema.TransformationRule();
        dto.setRuleName("val-ruleName");
        dto.setType(DataSchema.TransformationRule.TransformationType.TRIM);
        dto.setExpression("val-expression");
        dto.setEnabled(true);
        assertEquals("val-ruleName", dto.getRuleName());
        assertEquals(DataSchema.TransformationRule.TransformationType.TRIM, dto.getType());
        assertEquals("val-expression", dto.getExpression());
        assertTrue(dto.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        DataSchema.TransformationRule dto1 = DataSchema.TransformationRule.builder()
                        .ruleName("test-ruleName")
            .type(DataSchema.TransformationRule.TransformationType.TRIM)
            .parameters(Collections.emptyMap())
            .expression("test-expression")
            .enabled(true)
            .build();
        DataSchema.TransformationRule dto2 = DataSchema.TransformationRule.builder()
                        .ruleName("test-ruleName")
            .type(DataSchema.TransformationRule.TransformationType.TRIM)
            .parameters(Collections.emptyMap())
            .expression("test-expression")
            .enabled(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DataSchema.TransformationRule dto = DataSchema.TransformationRule.builder()
                        .ruleName("test-ruleName")
            .type(DataSchema.TransformationRule.TransformationType.TRIM)
            .parameters(Collections.emptyMap())
            .expression("test-expression")
            .enabled(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}