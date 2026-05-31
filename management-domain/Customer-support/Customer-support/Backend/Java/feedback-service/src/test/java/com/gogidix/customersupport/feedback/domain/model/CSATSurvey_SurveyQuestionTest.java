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
class CSATSurvey_SurveyQuestionTest {

        @Test
    void testBuilder() {
        CSATSurvey.SurveyQuestion dto = CSATSurvey.SurveyQuestion.builder()
                        .questionId("test-questionId")
            .text("test-text")
            .type(CSATSurvey.SurveyQuestion.QuestionType.RATING)
            .order(42)
            .required(true)
            .options(Collections.emptyList())
            .minRating(42)
            .maxRating(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-questionId", dto.getQuestionId());
        assertEquals("test-text", dto.getText());
        assertEquals(CSATSurvey.SurveyQuestion.QuestionType.RATING, dto.getType());
        assertEquals(42, dto.getOrder());
        assertTrue(dto.getRequired());
        assertEquals(42, dto.getMinRating());
        assertEquals(42, dto.getMaxRating());
    }

    @Test
    void testSettersAndGetters() {
        CSATSurvey.SurveyQuestion dto = new CSATSurvey.SurveyQuestion();
        dto.setQuestionId("val-questionId");
        dto.setText("val-text");
        dto.setType(CSATSurvey.SurveyQuestion.QuestionType.RATING);
        dto.setOrder(99);
        dto.setRequired(true);
        dto.setMinRating(99);
        dto.setMaxRating(99);
        assertEquals("val-questionId", dto.getQuestionId());
        assertEquals("val-text", dto.getText());
        assertEquals(CSATSurvey.SurveyQuestion.QuestionType.RATING, dto.getType());
        assertEquals(99, dto.getOrder());
        assertTrue(dto.getRequired());
        assertEquals(99, dto.getMinRating());
        assertEquals(99, dto.getMaxRating());
    }

    @Test
    void testEqualsAndHashCode() {
        CSATSurvey.SurveyQuestion dto1 = CSATSurvey.SurveyQuestion.builder()
                        .questionId("test-questionId")
            .text("test-text")
            .type(CSATSurvey.SurveyQuestion.QuestionType.RATING)
            .order(42)
            .required(true)
            .options(Collections.emptyList())
            .minRating(42)
            .maxRating(42)
            .build();
        CSATSurvey.SurveyQuestion dto2 = CSATSurvey.SurveyQuestion.builder()
                        .questionId("test-questionId")
            .text("test-text")
            .type(CSATSurvey.SurveyQuestion.QuestionType.RATING)
            .order(42)
            .required(true)
            .options(Collections.emptyList())
            .minRating(42)
            .maxRating(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CSATSurvey.SurveyQuestion dto = CSATSurvey.SurveyQuestion.builder()
                        .questionId("test-questionId")
            .text("test-text")
            .type(CSATSurvey.SurveyQuestion.QuestionType.RATING)
            .order(42)
            .required(true)
            .options(Collections.emptyList())
            .minRating(42)
            .maxRating(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}