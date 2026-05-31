package com.gogidix.digitalmarketing.marketingautomation.application.dto;

import com.gogidix.digitalmarketing.marketingautomation.application.dto.CampaignResponseDto;
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
class CampaignResponseDtoTest {

        @Test
    void testBuilder() {
        CampaignResponseDto dto = CampaignResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .status("test-status")
            .triggerType("test-triggerType")
            .triggerCondition("test-triggerCondition")
            .startDate("test-startDate")
            .endDate("test-endDate")
            .isActive("test-isActive")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-type", dto.getType());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-triggerType", dto.getTriggerType());
        assertEquals("test-triggerCondition", dto.getTriggerCondition());
        assertEquals("test-startDate", dto.getStartDate());
        assertEquals("test-endDate", dto.getEndDate());
        assertEquals("test-isActive", dto.getIsActive());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        CampaignResponseDto dto = new CampaignResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setType("val-type");
        dto.setStatus("val-status");
        dto.setTriggerType("val-triggerType");
        dto.setTriggerCondition("val-triggerCondition");
        dto.setStartDate("val-startDate");
        dto.setEndDate("val-endDate");
        dto.setIsActive("val-isActive");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-type", dto.getType());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-triggerType", dto.getTriggerType());
        assertEquals("val-triggerCondition", dto.getTriggerCondition());
        assertEquals("val-startDate", dto.getStartDate());
        assertEquals("val-endDate", dto.getEndDate());
        assertEquals("val-isActive", dto.getIsActive());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CampaignResponseDto dto1 = CampaignResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .status("test-status")
            .triggerType("test-triggerType")
            .triggerCondition("test-triggerCondition")
            .startDate("test-startDate")
            .endDate("test-endDate")
            .isActive("test-isActive")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CampaignResponseDto dto2 = CampaignResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .status("test-status")
            .triggerType("test-triggerType")
            .triggerCondition("test-triggerCondition")
            .startDate("test-startDate")
            .endDate("test-endDate")
            .isActive("test-isActive")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CampaignResponseDto dto = CampaignResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .type("test-type")
            .status("test-status")
            .triggerType("test-triggerType")
            .triggerCondition("test-triggerCondition")
            .startDate("test-startDate")
            .endDate("test-endDate")
            .isActive("test-isActive")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}