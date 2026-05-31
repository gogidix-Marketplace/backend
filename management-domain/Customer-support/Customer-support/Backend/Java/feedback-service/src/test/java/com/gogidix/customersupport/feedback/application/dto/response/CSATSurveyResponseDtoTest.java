package com.gogidix.customersupport.feedback.application.dto.response;

import com.gogidix.customersupport.feedback.application.dto.response.CSATSurveyResponseDto;
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
class CSATSurveyResponseDtoTest {

        @Test
    void testBuilder() {
        CSATSurveyResponseDto dto = CSATSurveyResponseDto.builder()
                        .id("test-id")
            .surveyId("test-surveyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ratingScale(42)
            .triggerEvents(Collections.emptyList())
            .triggerDelayHours(42)
            .locale("test-locale")
            .activeFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .activeUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .maxResponses(42)
            .responseCount(42)
            .createdBy("test-createdBy")
            .modifiedBy("test-modifiedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isActive(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-surveyId", dto.getSurveyId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getRatingScale());
        assertEquals(42, dto.getTriggerDelayHours());
        assertEquals("test-locale", dto.getLocale());
        assertEquals(42, dto.getMaxResponses());
        assertEquals(42, dto.getResponseCount());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-modifiedBy", dto.getModifiedBy());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        CSATSurveyResponseDto dto = new CSATSurveyResponseDto();
        dto.setId("val-id");
        dto.setSurveyId("val-surveyId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setRatingScale(99);
        dto.setTriggerDelayHours(99);
        dto.setLocale("val-locale");
        dto.setMaxResponses(99);
        dto.setResponseCount(99);
        dto.setCreatedBy("val-createdBy");
        dto.setModifiedBy("val-modifiedBy");
        dto.setIsActive(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-surveyId", dto.getSurveyId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getRatingScale());
        assertEquals(99, dto.getTriggerDelayHours());
        assertEquals("val-locale", dto.getLocale());
        assertEquals(99, dto.getMaxResponses());
        assertEquals(99, dto.getResponseCount());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-modifiedBy", dto.getModifiedBy());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        CSATSurveyResponseDto dto1 = CSATSurveyResponseDto.builder()
                        .id("test-id")
            .surveyId("test-surveyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ratingScale(42)
            .triggerEvents(Collections.emptyList())
            .triggerDelayHours(42)
            .locale("test-locale")
            .activeFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .activeUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .maxResponses(42)
            .responseCount(42)
            .createdBy("test-createdBy")
            .modifiedBy("test-modifiedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isActive(true)
            .build();
        CSATSurveyResponseDto dto2 = CSATSurveyResponseDto.builder()
                        .id("test-id")
            .surveyId("test-surveyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ratingScale(42)
            .triggerEvents(Collections.emptyList())
            .triggerDelayHours(42)
            .locale("test-locale")
            .activeFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .activeUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .maxResponses(42)
            .responseCount(42)
            .createdBy("test-createdBy")
            .modifiedBy("test-modifiedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isActive(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CSATSurveyResponseDto dto = CSATSurveyResponseDto.builder()
                        .id("test-id")
            .surveyId("test-surveyId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .ratingScale(42)
            .triggerEvents(Collections.emptyList())
            .triggerDelayHours(42)
            .locale("test-locale")
            .activeFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .activeUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .maxResponses(42)
            .responseCount(42)
            .createdBy("test-createdBy")
            .modifiedBy("test-modifiedBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isActive(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}