package com.gogidix.sales.onboarding.application.dto.response;

import com.gogidix.sales.onboarding.application.dto.response.OnboardingResponseDto;
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
class OnboardingResponseDto_OnboardingStepDtoTest {

        @Test
    void testBuilder() {
        OnboardingResponseDto.OnboardingStepDto dto = OnboardingResponseDto.OnboardingStepDto.builder()
                        .stepId("test-stepId")
            .onboardingId("test-onboardingId")
            .name("test-name")
            .description("test-description")
            .stepType(null)
            .status(null)
            .order(42)
            .optional(true)
            .autoComplete(true)
            .assignedTo("test-assignedTo")
            .assignedBy("test-assignedBy")
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .durationMinutes(42L)
            .completedBy("test-completedBy")
            .updatedBy("test-updatedBy")
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dependencies(Collections.emptyList())
            .notes("test-notes")
            .estimatedDurationMinutes(42)
            .helpUrl("test-helpUrl")
            .build();
        assertNotNull(dto);
        assertEquals("test-stepId", dto.getStepId());
        assertEquals("test-onboardingId", dto.getOnboardingId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getOrder());
        assertTrue(dto.getOptional());
        assertTrue(dto.getAutoComplete());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals("test-assignedBy", dto.getAssignedBy());
        assertEquals(42L, dto.getDurationMinutes());
        assertEquals("test-completedBy", dto.getCompletedBy());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(42, dto.getEstimatedDurationMinutes());
        assertEquals("test-helpUrl", dto.getHelpUrl());
    }

    @Test
    void testSettersAndGetters() {
        OnboardingResponseDto.OnboardingStepDto dto = new OnboardingResponseDto.OnboardingStepDto();
        dto.setStepId("val-stepId");
        dto.setOnboardingId("val-onboardingId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setOrder(99);
        dto.setOptional(true);
        dto.setAutoComplete(true);
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedBy("val-assignedBy");
        dto.setCompletedBy("val-completedBy");
        dto.setUpdatedBy("val-updatedBy");
        dto.setNotes("val-notes");
        dto.setEstimatedDurationMinutes(99);
        dto.setHelpUrl("val-helpUrl");
        assertEquals("val-stepId", dto.getStepId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getOrder());
        assertTrue(dto.getOptional());
        assertTrue(dto.getAutoComplete());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedBy", dto.getAssignedBy());
        assertEquals("val-completedBy", dto.getCompletedBy());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(99, dto.getEstimatedDurationMinutes());
        assertEquals("val-helpUrl", dto.getHelpUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingResponseDto.OnboardingStepDto dto1 = OnboardingResponseDto.OnboardingStepDto.builder()
                        .stepId("test-stepId")
            .onboardingId("test-onboardingId")
            .name("test-name")
            .description("test-description")
            .stepType(null)
            .status(null)
            .order(42)
            .optional(true)
            .autoComplete(true)
            .assignedTo("test-assignedTo")
            .assignedBy("test-assignedBy")
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .durationMinutes(42L)
            .completedBy("test-completedBy")
            .updatedBy("test-updatedBy")
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dependencies(Collections.emptyList())
            .notes("test-notes")
            .estimatedDurationMinutes(42)
            .helpUrl("test-helpUrl")
            .build();
        OnboardingResponseDto.OnboardingStepDto dto2 = OnboardingResponseDto.OnboardingStepDto.builder()
                        .stepId("test-stepId")
            .onboardingId("test-onboardingId")
            .name("test-name")
            .description("test-description")
            .stepType(null)
            .status(null)
            .order(42)
            .optional(true)
            .autoComplete(true)
            .assignedTo("test-assignedTo")
            .assignedBy("test-assignedBy")
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .durationMinutes(42L)
            .completedBy("test-completedBy")
            .updatedBy("test-updatedBy")
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dependencies(Collections.emptyList())
            .notes("test-notes")
            .estimatedDurationMinutes(42)
            .helpUrl("test-helpUrl")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        OnboardingResponseDto.OnboardingStepDto dto = OnboardingResponseDto.OnboardingStepDto.builder()
                        .stepId("test-stepId")
            .onboardingId("test-onboardingId")
            .name("test-name")
            .description("test-description")
            .stepType(null)
            .status(null)
            .order(42)
            .optional(true)
            .autoComplete(true)
            .assignedTo("test-assignedTo")
            .assignedBy("test-assignedBy")
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .durationMinutes(42L)
            .completedBy("test-completedBy")
            .updatedBy("test-updatedBy")
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dependencies(Collections.emptyList())
            .notes("test-notes")
            .estimatedDurationMinutes(42)
            .helpUrl("test-helpUrl")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}