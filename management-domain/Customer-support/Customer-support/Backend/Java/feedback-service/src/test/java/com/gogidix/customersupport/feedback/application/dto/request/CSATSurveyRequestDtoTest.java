package com.gogidix.customersupport.feedback.application.dto.request;

import com.gogidix.customersupport.feedback.application.dto.request.CSATSurveyRequestDto;
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
class CSATSurveyRequestDtoTest {

        @Test
    void testBuilder() {
        CSATSurveyRequestDto dto = CSATSurveyRequestDto.builder()
                        .name("test-name")
            .description("test-description")
            .ratingScale(42)
            .triggerEvents(Collections.emptyList())
            .triggerDelayHours(42)
            .locale("test-locale")
            .activeFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .activeUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .maxResponses(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getRatingScale());
        assertEquals(42, dto.getTriggerDelayHours());
        assertEquals("test-locale", dto.getLocale());
        assertEquals(42, dto.getMaxResponses());
    }

    @Test
    void testSettersAndGetters() {
        CSATSurveyRequestDto dto = new CSATSurveyRequestDto();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setRatingScale(99);
        dto.setTriggerDelayHours(99);
        dto.setLocale("val-locale");
        dto.setMaxResponses(99);
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getRatingScale());
        assertEquals(99, dto.getTriggerDelayHours());
        assertEquals("val-locale", dto.getLocale());
        assertEquals(99, dto.getMaxResponses());
    }

    @Test
    void testEqualsAndHashCode() {
        CSATSurveyRequestDto dto1 = CSATSurveyRequestDto.builder()
                        .name("test-name")
            .description("test-description")
            .ratingScale(42)
            .triggerEvents(Collections.emptyList())
            .triggerDelayHours(42)
            .locale("test-locale")
            .activeFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .activeUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .maxResponses(42)
            .build();
        CSATSurveyRequestDto dto2 = CSATSurveyRequestDto.builder()
                        .name("test-name")
            .description("test-description")
            .ratingScale(42)
            .triggerEvents(Collections.emptyList())
            .triggerDelayHours(42)
            .locale("test-locale")
            .activeFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .activeUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .maxResponses(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CSATSurveyRequestDto dto = CSATSurveyRequestDto.builder()
                        .name("test-name")
            .description("test-description")
            .ratingScale(42)
            .triggerEvents(Collections.emptyList())
            .triggerDelayHours(42)
            .locale("test-locale")
            .activeFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .activeUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .maxResponses(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}