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
class OnboardingResponseDto_DocumentChecklistDtoTest {

        @Test
    void testBuilder() {
        OnboardingResponseDto.DocumentChecklistDto dto = OnboardingResponseDto.DocumentChecklistDto.builder()
                        .id("test-id")
            .checklistId("test-checklistId")
            .onboardingId("test-onboardingId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .documents(Collections.emptyList())
            .requireAllDocuments(true)
            .totalRequired(42)
            .totalCompleted(42)
            .completed(true)
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-checklistId", dto.getChecklistId());
        assertEquals("test-onboardingId", dto.getOnboardingId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertTrue(dto.getRequireAllDocuments());
        assertEquals(42, dto.getTotalRequired());
        assertEquals(42, dto.getTotalCompleted());
        assertTrue(dto.getCompleted());
    }

    @Test
    void testSettersAndGetters() {
        OnboardingResponseDto.DocumentChecklistDto dto = new OnboardingResponseDto.DocumentChecklistDto();
        dto.setId("val-id");
        dto.setChecklistId("val-checklistId");
        dto.setOnboardingId("val-onboardingId");
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setRequireAllDocuments(true);
        dto.setTotalRequired(99);
        dto.setTotalCompleted(99);
        dto.setCompleted(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-checklistId", dto.getChecklistId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertTrue(dto.getRequireAllDocuments());
        assertEquals(99, dto.getTotalRequired());
        assertEquals(99, dto.getTotalCompleted());
        assertTrue(dto.getCompleted());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingResponseDto.DocumentChecklistDto dto1 = OnboardingResponseDto.DocumentChecklistDto.builder()
                        .id("test-id")
            .checklistId("test-checklistId")
            .onboardingId("test-onboardingId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .documents(Collections.emptyList())
            .requireAllDocuments(true)
            .totalRequired(42)
            .totalCompleted(42)
            .completed(true)
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        OnboardingResponseDto.DocumentChecklistDto dto2 = OnboardingResponseDto.DocumentChecklistDto.builder()
                        .id("test-id")
            .checklistId("test-checklistId")
            .onboardingId("test-onboardingId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .documents(Collections.emptyList())
            .requireAllDocuments(true)
            .totalRequired(42)
            .totalCompleted(42)
            .completed(true)
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        OnboardingResponseDto.DocumentChecklistDto dto = OnboardingResponseDto.DocumentChecklistDto.builder()
                        .id("test-id")
            .checklistId("test-checklistId")
            .onboardingId("test-onboardingId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .documents(Collections.emptyList())
            .requireAllDocuments(true)
            .totalRequired(42)
            .totalCompleted(42)
            .completed(true)
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}