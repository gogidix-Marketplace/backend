package com.gogidix.sales.onboarding.application.dto.response;

import com.gogidix.sales.onboarding.application.dto.response.OnboardingTemplateResponseDto;
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
class OnboardingTemplateResponseDto_TemplateStepDtoTest {

        @Test
    void testBuilder() {
        OnboardingTemplateResponseDto.TemplateStepDto dto = OnboardingTemplateResponseDto.TemplateStepDto.builder()
                        .stepId("test-stepId")
            .name("test-name")
            .description("test-description")
            .stepType("test-stepType")
            .order(42)
            .optional(true)
            .autoComplete(true)
            .dependencies(Collections.emptyList())
            .estimatedDurationMinutes(42)
            .helpUrl("test-helpUrl")
            .requiredFields(Collections.emptyList())
            .assigneeRole("test-assigneeRole")
            .build();
        assertNotNull(dto);
        assertEquals("test-stepId", dto.getStepId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-stepType", dto.getStepType());
        assertEquals(42, dto.getOrder());
        assertTrue(dto.getOptional());
        assertTrue(dto.getAutoComplete());
        assertEquals(42, dto.getEstimatedDurationMinutes());
        assertEquals("test-helpUrl", dto.getHelpUrl());
        assertEquals("test-assigneeRole", dto.getAssigneeRole());
    }

    @Test
    void testSettersAndGetters() {
        OnboardingTemplateResponseDto.TemplateStepDto dto = new OnboardingTemplateResponseDto.TemplateStepDto();
        dto.setStepId("val-stepId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setStepType("val-stepType");
        dto.setOrder(99);
        dto.setOptional(true);
        dto.setAutoComplete(true);
        dto.setEstimatedDurationMinutes(99);
        dto.setHelpUrl("val-helpUrl");
        dto.setAssigneeRole("val-assigneeRole");
        assertEquals("val-stepId", dto.getStepId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-stepType", dto.getStepType());
        assertEquals(99, dto.getOrder());
        assertTrue(dto.getOptional());
        assertTrue(dto.getAutoComplete());
        assertEquals(99, dto.getEstimatedDurationMinutes());
        assertEquals("val-helpUrl", dto.getHelpUrl());
        assertEquals("val-assigneeRole", dto.getAssigneeRole());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingTemplateResponseDto.TemplateStepDto dto1 = OnboardingTemplateResponseDto.TemplateStepDto.builder()
                        .stepId("test-stepId")
            .name("test-name")
            .description("test-description")
            .stepType("test-stepType")
            .order(42)
            .optional(true)
            .autoComplete(true)
            .dependencies(Collections.emptyList())
            .estimatedDurationMinutes(42)
            .helpUrl("test-helpUrl")
            .requiredFields(Collections.emptyList())
            .assigneeRole("test-assigneeRole")
            .build();
        OnboardingTemplateResponseDto.TemplateStepDto dto2 = OnboardingTemplateResponseDto.TemplateStepDto.builder()
                        .stepId("test-stepId")
            .name("test-name")
            .description("test-description")
            .stepType("test-stepType")
            .order(42)
            .optional(true)
            .autoComplete(true)
            .dependencies(Collections.emptyList())
            .estimatedDurationMinutes(42)
            .helpUrl("test-helpUrl")
            .requiredFields(Collections.emptyList())
            .assigneeRole("test-assigneeRole")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        OnboardingTemplateResponseDto.TemplateStepDto dto = OnboardingTemplateResponseDto.TemplateStepDto.builder()
                        .stepId("test-stepId")
            .name("test-name")
            .description("test-description")
            .stepType("test-stepType")
            .order(42)
            .optional(true)
            .autoComplete(true)
            .dependencies(Collections.emptyList())
            .estimatedDurationMinutes(42)
            .helpUrl("test-helpUrl")
            .requiredFields(Collections.emptyList())
            .assigneeRole("test-assigneeRole")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}