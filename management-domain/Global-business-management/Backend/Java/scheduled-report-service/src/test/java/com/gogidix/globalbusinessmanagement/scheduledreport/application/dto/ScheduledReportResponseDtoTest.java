package com.gogidix.globalbusinessmanagement.scheduledreport.application.dto;

import com.gogidix.globalbusinessmanagement.scheduledreport.application.dto.ScheduledReportResponseDto;
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
class ScheduledReportResponseDtoTest {

        @Test
    void testBuilder() {
        ScheduledReportResponseDto dto = ScheduledReportResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .reportId("test-reportId")
            .cronExpression("test-cronExpression")
            .recipients("test-recipients")
            .format("test-format")
            .isActive("test-isActive")
            .lastRunAt("test-lastRunAt")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-reportId", dto.getReportId());
        assertEquals("test-cronExpression", dto.getCronExpression());
        assertEquals("test-recipients", dto.getRecipients());
        assertEquals("test-format", dto.getFormat());
        assertEquals("test-isActive", dto.getIsActive());
        assertEquals("test-lastRunAt", dto.getLastRunAt());
    }

    @Test
    void testSettersAndGetters() {
        ScheduledReportResponseDto dto = new ScheduledReportResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setReportId("val-reportId");
        dto.setCronExpression("val-cronExpression");
        dto.setRecipients("val-recipients");
        dto.setFormat("val-format");
        dto.setIsActive("val-isActive");
        dto.setLastRunAt("val-lastRunAt");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-reportId", dto.getReportId());
        assertEquals("val-cronExpression", dto.getCronExpression());
        assertEquals("val-recipients", dto.getRecipients());
        assertEquals("val-format", dto.getFormat());
        assertEquals("val-isActive", dto.getIsActive());
        assertEquals("val-lastRunAt", dto.getLastRunAt());
    }

    @Test
    void testEqualsAndHashCode() {
        ScheduledReportResponseDto dto1 = ScheduledReportResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .reportId("test-reportId")
            .cronExpression("test-cronExpression")
            .recipients("test-recipients")
            .format("test-format")
            .isActive("test-isActive")
            .lastRunAt("test-lastRunAt")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ScheduledReportResponseDto dto2 = ScheduledReportResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .reportId("test-reportId")
            .cronExpression("test-cronExpression")
            .recipients("test-recipients")
            .format("test-format")
            .isActive("test-isActive")
            .lastRunAt("test-lastRunAt")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ScheduledReportResponseDto dto = ScheduledReportResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .reportId("test-reportId")
            .cronExpression("test-cronExpression")
            .recipients("test-recipients")
            .format("test-format")
            .isActive("test-isActive")
            .lastRunAt("test-lastRunAt")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}