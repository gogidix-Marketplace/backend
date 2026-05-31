package com.gogidix.customersupport.feedback.domain.model;

import com.gogidix.customersupport.feedback.domain.model.CSATSurvey;
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
class CSATSurvey_QuestionOptionTest {

        @Test
    void testBuilder() {
        CSATSurvey.QuestionOption dto = CSATSurvey.QuestionOption.builder()
                        .value("test-value")
            .label("test-label")
            .order(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-value", dto.getValue());
        assertEquals("test-label", dto.getLabel());
        assertEquals(42, dto.getOrder());
    }

    @Test
    void testSettersAndGetters() {
        CSATSurvey.QuestionOption dto = new CSATSurvey.QuestionOption();
        dto.setValue("val-value");
        dto.setLabel("val-label");
        dto.setOrder(99);
        assertEquals("val-value", dto.getValue());
        assertEquals("val-label", dto.getLabel());
        assertEquals(99, dto.getOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        CSATSurvey.QuestionOption dto1 = CSATSurvey.QuestionOption.builder()
                        .value("test-value")
            .label("test-label")
            .order(42)
            .build();
        CSATSurvey.QuestionOption dto2 = CSATSurvey.QuestionOption.builder()
                        .value("test-value")
            .label("test-label")
            .order(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CSATSurvey.QuestionOption dto = CSATSurvey.QuestionOption.builder()
                        .value("test-value")
            .label("test-label")
            .order(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}