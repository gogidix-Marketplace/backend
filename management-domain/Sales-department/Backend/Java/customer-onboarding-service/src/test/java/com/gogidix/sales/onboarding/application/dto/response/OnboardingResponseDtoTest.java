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
class OnboardingResponseDtoTest {

        @Test
    void testBuilder() {
        OnboardingResponseDto dto = OnboardingResponseDto.builder()
                        .id("test-id")
            .onboardingId("test-onboardingId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerType(OnboardingResponseDto.CustomerTypeDto.INDIVIDUAL)
            .templateId("test-templateId")
            .templateName("test-templateName")
            .status(OnboardingResponseDto.OnboardingStatusDto.NOT_STARTED)
            .priority(OnboardingResponseDto.PriorityDto.LOW)
            .assignedTo("test-assignedTo")
            .assignedBy("test-assignedBy")
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .estimatedCompletionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .estimatedDurationHours(42)
            .actualDurationMinutes(42L)
            .initiatedBy("test-initiatedBy")
            .completedBy("test-completedBy")
            .notes("test-notes")
            .cancellationReason("test-cancellationReason")
            .steps(Collections.emptyList())
            .documentChecklist(null)
            .metadata(Collections.emptyMap())
            .progressPercentage(42)
            .lastProgressUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-onboardingId", dto.getOnboardingId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-customerEmail", dto.getCustomerEmail());
        assertEquals(OnboardingResponseDto.CustomerTypeDto.INDIVIDUAL, dto.getCustomerType());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-templateName", dto.getTemplateName());
        assertEquals(OnboardingResponseDto.OnboardingStatusDto.NOT_STARTED, dto.getStatus());
        assertEquals(OnboardingResponseDto.PriorityDto.LOW, dto.getPriority());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals("test-assignedBy", dto.getAssignedBy());
        assertEquals(42, dto.getEstimatedDurationHours());
        assertEquals(42L, dto.getActualDurationMinutes());
        assertEquals("test-initiatedBy", dto.getInitiatedBy());
        assertEquals("test-completedBy", dto.getCompletedBy());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-cancellationReason", dto.getCancellationReason());
        assertEquals(42, dto.getProgressPercentage());
    }

    @Test
    void testSettersAndGetters() {
        OnboardingResponseDto dto = new OnboardingResponseDto();
        dto.setId("val-id");
        dto.setOnboardingId("val-onboardingId");
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setCustomerType(OnboardingResponseDto.CustomerTypeDto.INDIVIDUAL);
        dto.setTemplateId("val-templateId");
        dto.setTemplateName("val-templateName");
        dto.setStatus(OnboardingResponseDto.OnboardingStatusDto.NOT_STARTED);
        dto.setPriority(OnboardingResponseDto.PriorityDto.LOW);
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedBy("val-assignedBy");
        dto.setEstimatedDurationHours(99);
        dto.setInitiatedBy("val-initiatedBy");
        dto.setCompletedBy("val-completedBy");
        dto.setNotes("val-notes");
        dto.setCancellationReason("val-cancellationReason");
        dto.setProgressPercentage(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals(OnboardingResponseDto.CustomerTypeDto.INDIVIDUAL, dto.getCustomerType());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-templateName", dto.getTemplateName());
        assertEquals(OnboardingResponseDto.OnboardingStatusDto.NOT_STARTED, dto.getStatus());
        assertEquals(OnboardingResponseDto.PriorityDto.LOW, dto.getPriority());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedBy", dto.getAssignedBy());
        assertEquals(99, dto.getEstimatedDurationHours());
        assertEquals("val-initiatedBy", dto.getInitiatedBy());
        assertEquals("val-completedBy", dto.getCompletedBy());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-cancellationReason", dto.getCancellationReason());
        assertEquals(99, dto.getProgressPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingResponseDto dto1 = OnboardingResponseDto.builder()
                        .id("test-id")
            .onboardingId("test-onboardingId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerType(OnboardingResponseDto.CustomerTypeDto.INDIVIDUAL)
            .templateId("test-templateId")
            .templateName("test-templateName")
            .status(OnboardingResponseDto.OnboardingStatusDto.NOT_STARTED)
            .priority(OnboardingResponseDto.PriorityDto.LOW)
            .assignedTo("test-assignedTo")
            .assignedBy("test-assignedBy")
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .estimatedCompletionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .estimatedDurationHours(42)
            .actualDurationMinutes(42L)
            .initiatedBy("test-initiatedBy")
            .completedBy("test-completedBy")
            .notes("test-notes")
            .cancellationReason("test-cancellationReason")
            .steps(Collections.emptyList())
            .documentChecklist(null)
            .metadata(Collections.emptyMap())
            .progressPercentage(42)
            .lastProgressUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        OnboardingResponseDto dto2 = OnboardingResponseDto.builder()
                        .id("test-id")
            .onboardingId("test-onboardingId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerType(OnboardingResponseDto.CustomerTypeDto.INDIVIDUAL)
            .templateId("test-templateId")
            .templateName("test-templateName")
            .status(OnboardingResponseDto.OnboardingStatusDto.NOT_STARTED)
            .priority(OnboardingResponseDto.PriorityDto.LOW)
            .assignedTo("test-assignedTo")
            .assignedBy("test-assignedBy")
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .estimatedCompletionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .estimatedDurationHours(42)
            .actualDurationMinutes(42L)
            .initiatedBy("test-initiatedBy")
            .completedBy("test-completedBy")
            .notes("test-notes")
            .cancellationReason("test-cancellationReason")
            .steps(Collections.emptyList())
            .documentChecklist(null)
            .metadata(Collections.emptyMap())
            .progressPercentage(42)
            .lastProgressUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        OnboardingResponseDto dto = OnboardingResponseDto.builder()
                        .id("test-id")
            .onboardingId("test-onboardingId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerType(OnboardingResponseDto.CustomerTypeDto.INDIVIDUAL)
            .templateId("test-templateId")
            .templateName("test-templateName")
            .status(OnboardingResponseDto.OnboardingStatusDto.NOT_STARTED)
            .priority(OnboardingResponseDto.PriorityDto.LOW)
            .assignedTo("test-assignedTo")
            .assignedBy("test-assignedBy")
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .estimatedCompletionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .estimatedDurationHours(42)
            .actualDurationMinutes(42L)
            .initiatedBy("test-initiatedBy")
            .completedBy("test-completedBy")
            .notes("test-notes")
            .cancellationReason("test-cancellationReason")
            .steps(Collections.emptyList())
            .documentChecklist(null)
            .metadata(Collections.emptyMap())
            .progressPercentage(42)
            .lastProgressUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}