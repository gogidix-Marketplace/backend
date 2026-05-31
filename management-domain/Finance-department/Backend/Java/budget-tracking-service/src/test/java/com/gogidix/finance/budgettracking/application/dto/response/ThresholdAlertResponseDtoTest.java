package com.gogidix.finance.budgettracking.application.dto.response;

import com.gogidix.finance.budgettracking.application.dto.response.ThresholdAlertResponseDto;
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
class ThresholdAlertResponseDtoTest {

        @Test
    void testBuilder() {
        ThresholdAlertResponseDto dto = ThresholdAlertResponseDto.builder()
                        .id("test-id")
            .alertId("test-alertId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .alertName("test-alertName")
            .description("test-description")
            .alertType(ThresholdAlertResponseDto.AlertTypeDto.UTILIZATION)
            .thresholdType(ThresholdAlertResponseDto.ThresholdTypeDto.PERCENTAGE)
            .thresholdValue(BigDecimal.TEN)
            .thresholdLevel(ThresholdAlertResponseDto.ThresholdLevelDto.INFO)
            .enabled(true)
            .status(ThresholdAlertResponseDto.AlertStatusDto.ACTIVE)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .createdBy("test-createdBy")
            .modifiedBy("test-modifiedBy")
            .effectiveFrom(LocalDate.of(2025,1,15))
            .effectiveTo(LocalDate.of(2025,1,15))
            .frequency(ThresholdAlertResponseDto.AlertFrequencyDto.IMMEDIATE)
            .recurring(true)
            .recurrencePattern("test-recurrencePattern")
            .recipients(Collections.emptyList())
            .recipientGroups(Collections.emptyList())
            .notificationChannel("test-notificationChannel")
            .requireAcknowledgement(true)
            .escalationLevel(42)
            .escalateTo("test-escalateTo")
            .escalationThreshold(BigDecimal.TEN)
            .lastTriggeredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .triggerCount(42)
            .lastAcknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastAcknowledgedBy("test-lastAcknowledgedBy")
            .templateId("test-templateId")
            .customMessage("test-customMessage")
            .history(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-alertId", dto.getAlertId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-budgetId", dto.getBudgetId());
        assertEquals("test-budgetCode", dto.getBudgetCode());
        assertEquals("test-alertName", dto.getAlertName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(ThresholdAlertResponseDto.AlertTypeDto.UTILIZATION, dto.getAlertType());
        assertEquals(ThresholdAlertResponseDto.ThresholdTypeDto.PERCENTAGE, dto.getThresholdType());
        assertEquals(BigDecimal.TEN, dto.getThresholdValue());
        assertEquals(ThresholdAlertResponseDto.ThresholdLevelDto.INFO, dto.getThresholdLevel());
        assertTrue(dto.isEnabled());
        assertEquals(ThresholdAlertResponseDto.AlertStatusDto.ACTIVE, dto.getStatus());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-modifiedBy", dto.getModifiedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveFrom());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveTo());
        assertEquals(ThresholdAlertResponseDto.AlertFrequencyDto.IMMEDIATE, dto.getFrequency());
        assertTrue(dto.isRecurring());
        assertEquals("test-recurrencePattern", dto.getRecurrencePattern());
        assertEquals("test-notificationChannel", dto.getNotificationChannel());
        assertTrue(dto.isRequireAcknowledgement());
        assertEquals(42, dto.getEscalationLevel());
        assertEquals("test-escalateTo", dto.getEscalateTo());
        assertEquals(BigDecimal.TEN, dto.getEscalationThreshold());
        assertEquals(42, dto.getTriggerCount());
        assertEquals("test-lastAcknowledgedBy", dto.getLastAcknowledgedBy());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-customMessage", dto.getCustomMessage());
    }

    @Test
    void testSettersAndGetters() {
        ThresholdAlertResponseDto dto = new ThresholdAlertResponseDto();
        dto.setId("val-id");
        dto.setAlertId("val-alertId");
        dto.setTenantId("val-tenantId");
        dto.setBudgetId("val-budgetId");
        dto.setBudgetCode("val-budgetCode");
        dto.setAlertName("val-alertName");
        dto.setDescription("val-description");
        dto.setAlertType(ThresholdAlertResponseDto.AlertTypeDto.UTILIZATION);
        dto.setThresholdType(ThresholdAlertResponseDto.ThresholdTypeDto.PERCENTAGE);
        dto.setThresholdValue(BigDecimal.ONE);
        dto.setThresholdLevel(ThresholdAlertResponseDto.ThresholdLevelDto.INFO);
        dto.setEnabled(true);
        dto.setStatus(ThresholdAlertResponseDto.AlertStatusDto.ACTIVE);
        dto.setCategory("val-category");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setCreatedBy("val-createdBy");
        dto.setModifiedBy("val-modifiedBy");
        dto.setEffectiveFrom(LocalDate.of(2025,6,1));
        dto.setEffectiveTo(LocalDate.of(2025,6,1));
        dto.setFrequency(ThresholdAlertResponseDto.AlertFrequencyDto.IMMEDIATE);
        dto.setRecurring(true);
        dto.setRecurrencePattern("val-recurrencePattern");
        dto.setNotificationChannel("val-notificationChannel");
        dto.setRequireAcknowledgement(true);
        dto.setEscalationLevel(99);
        dto.setEscalateTo("val-escalateTo");
        dto.setEscalationThreshold(BigDecimal.ONE);
        dto.setTriggerCount(99);
        dto.setLastAcknowledgedBy("val-lastAcknowledgedBy");
        dto.setTemplateId("val-templateId");
        dto.setCustomMessage("val-customMessage");
        assertEquals("val-id", dto.getId());
        assertEquals("val-alertId", dto.getAlertId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-budgetId", dto.getBudgetId());
        assertEquals("val-budgetCode", dto.getBudgetCode());
        assertEquals("val-alertName", dto.getAlertName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(ThresholdAlertResponseDto.AlertTypeDto.UTILIZATION, dto.getAlertType());
        assertEquals(ThresholdAlertResponseDto.ThresholdTypeDto.PERCENTAGE, dto.getThresholdType());
        assertEquals(BigDecimal.ONE, dto.getThresholdValue());
        assertEquals(ThresholdAlertResponseDto.ThresholdLevelDto.INFO, dto.getThresholdLevel());
        assertTrue(dto.isEnabled());
        assertEquals(ThresholdAlertResponseDto.AlertStatusDto.ACTIVE, dto.getStatus());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-modifiedBy", dto.getModifiedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveFrom());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveTo());
        assertEquals(ThresholdAlertResponseDto.AlertFrequencyDto.IMMEDIATE, dto.getFrequency());
        assertTrue(dto.isRecurring());
        assertEquals("val-recurrencePattern", dto.getRecurrencePattern());
        assertEquals("val-notificationChannel", dto.getNotificationChannel());
        assertTrue(dto.isRequireAcknowledgement());
        assertEquals(99, dto.getEscalationLevel());
        assertEquals("val-escalateTo", dto.getEscalateTo());
        assertEquals(BigDecimal.ONE, dto.getEscalationThreshold());
        assertEquals(99, dto.getTriggerCount());
        assertEquals("val-lastAcknowledgedBy", dto.getLastAcknowledgedBy());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-customMessage", dto.getCustomMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ThresholdAlertResponseDto dto1 = ThresholdAlertResponseDto.builder()
                        .id("test-id")
            .alertId("test-alertId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .alertName("test-alertName")
            .description("test-description")
            .alertType(ThresholdAlertResponseDto.AlertTypeDto.UTILIZATION)
            .thresholdType(ThresholdAlertResponseDto.ThresholdTypeDto.PERCENTAGE)
            .thresholdValue(BigDecimal.TEN)
            .thresholdLevel(ThresholdAlertResponseDto.ThresholdLevelDto.INFO)
            .enabled(true)
            .status(ThresholdAlertResponseDto.AlertStatusDto.ACTIVE)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .createdBy("test-createdBy")
            .modifiedBy("test-modifiedBy")
            .effectiveFrom(LocalDate.of(2025,1,15))
            .effectiveTo(LocalDate.of(2025,1,15))
            .frequency(ThresholdAlertResponseDto.AlertFrequencyDto.IMMEDIATE)
            .recurring(true)
            .recurrencePattern("test-recurrencePattern")
            .recipients(Collections.emptyList())
            .recipientGroups(Collections.emptyList())
            .notificationChannel("test-notificationChannel")
            .requireAcknowledgement(true)
            .escalationLevel(42)
            .escalateTo("test-escalateTo")
            .escalationThreshold(BigDecimal.TEN)
            .lastTriggeredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .triggerCount(42)
            .lastAcknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastAcknowledgedBy("test-lastAcknowledgedBy")
            .templateId("test-templateId")
            .customMessage("test-customMessage")
            .history(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ThresholdAlertResponseDto dto2 = ThresholdAlertResponseDto.builder()
                        .id("test-id")
            .alertId("test-alertId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .alertName("test-alertName")
            .description("test-description")
            .alertType(ThresholdAlertResponseDto.AlertTypeDto.UTILIZATION)
            .thresholdType(ThresholdAlertResponseDto.ThresholdTypeDto.PERCENTAGE)
            .thresholdValue(BigDecimal.TEN)
            .thresholdLevel(ThresholdAlertResponseDto.ThresholdLevelDto.INFO)
            .enabled(true)
            .status(ThresholdAlertResponseDto.AlertStatusDto.ACTIVE)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .createdBy("test-createdBy")
            .modifiedBy("test-modifiedBy")
            .effectiveFrom(LocalDate.of(2025,1,15))
            .effectiveTo(LocalDate.of(2025,1,15))
            .frequency(ThresholdAlertResponseDto.AlertFrequencyDto.IMMEDIATE)
            .recurring(true)
            .recurrencePattern("test-recurrencePattern")
            .recipients(Collections.emptyList())
            .recipientGroups(Collections.emptyList())
            .notificationChannel("test-notificationChannel")
            .requireAcknowledgement(true)
            .escalationLevel(42)
            .escalateTo("test-escalateTo")
            .escalationThreshold(BigDecimal.TEN)
            .lastTriggeredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .triggerCount(42)
            .lastAcknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastAcknowledgedBy("test-lastAcknowledgedBy")
            .templateId("test-templateId")
            .customMessage("test-customMessage")
            .history(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ThresholdAlertResponseDto dto = ThresholdAlertResponseDto.builder()
                        .id("test-id")
            .alertId("test-alertId")
            .tenantId("test-tenantId")
            .budgetId("test-budgetId")
            .budgetCode("test-budgetCode")
            .alertName("test-alertName")
            .description("test-description")
            .alertType(ThresholdAlertResponseDto.AlertTypeDto.UTILIZATION)
            .thresholdType(ThresholdAlertResponseDto.ThresholdTypeDto.PERCENTAGE)
            .thresholdValue(BigDecimal.TEN)
            .thresholdLevel(ThresholdAlertResponseDto.ThresholdLevelDto.INFO)
            .enabled(true)
            .status(ThresholdAlertResponseDto.AlertStatusDto.ACTIVE)
            .category("test-category")
            .department("test-department")
            .costCenter("test-costCenter")
            .createdBy("test-createdBy")
            .modifiedBy("test-modifiedBy")
            .effectiveFrom(LocalDate.of(2025,1,15))
            .effectiveTo(LocalDate.of(2025,1,15))
            .frequency(ThresholdAlertResponseDto.AlertFrequencyDto.IMMEDIATE)
            .recurring(true)
            .recurrencePattern("test-recurrencePattern")
            .recipients(Collections.emptyList())
            .recipientGroups(Collections.emptyList())
            .notificationChannel("test-notificationChannel")
            .requireAcknowledgement(true)
            .escalationLevel(42)
            .escalateTo("test-escalateTo")
            .escalationThreshold(BigDecimal.TEN)
            .lastTriggeredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .triggerCount(42)
            .lastAcknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastAcknowledgedBy("test-lastAcknowledgedBy")
            .templateId("test-templateId")
            .customMessage("test-customMessage")
            .history(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}