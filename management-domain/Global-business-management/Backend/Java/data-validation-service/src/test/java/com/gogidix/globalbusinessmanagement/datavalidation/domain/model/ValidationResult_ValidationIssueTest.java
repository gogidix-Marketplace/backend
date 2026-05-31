package com.gogidix.globalbusinessmanagement.datavalidation.domain.model;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationResult;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule;
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
class ValidationResult_ValidationIssueTest {

        @Test
    void testBuilder() {
        ValidationResult.ValidationIssue dto = ValidationResult.ValidationIssue.builder()
                        .code("test-code")
            .field("test-field")
            .message("test-message")
            .severity(ValidationRule.SeverityLevel.CRITICAL)
            .actualValue(null)
            .expectedValue(null)
            .details(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-code", dto.getCode());
        assertEquals("test-field", dto.getField());
        assertEquals("test-message", dto.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        ValidationResult.ValidationIssue dto = new ValidationResult.ValidationIssue();
        dto.setCode("val-code");
        dto.setField("val-field");
        dto.setMessage("val-message");
        assertEquals("val-code", dto.getCode());
        assertEquals("val-field", dto.getField());
        assertEquals("val-message", dto.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ValidationResult.ValidationIssue dto1 = ValidationResult.ValidationIssue.builder()
                        .code("test-code")
            .field("test-field")
            .message("test-message")
            .severity(ValidationRule.SeverityLevel.CRITICAL)
            .actualValue(null)
            .expectedValue(null)
            .details(Collections.emptyMap())
            .build();
        ValidationResult.ValidationIssue dto2 = ValidationResult.ValidationIssue.builder()
                        .code("test-code")
            .field("test-field")
            .message("test-message")
            .severity(ValidationRule.SeverityLevel.CRITICAL)
            .actualValue(null)
            .expectedValue(null)
            .details(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ValidationResult.ValidationIssue dto = ValidationResult.ValidationIssue.builder()
                        .code("test-code")
            .field("test-field")
            .message("test-message")
            .severity(ValidationRule.SeverityLevel.CRITICAL)
            .actualValue(null)
            .expectedValue(null)
            .details(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}