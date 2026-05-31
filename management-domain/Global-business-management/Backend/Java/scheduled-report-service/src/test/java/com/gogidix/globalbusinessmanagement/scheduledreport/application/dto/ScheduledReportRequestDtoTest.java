package com.gogidix.globalbusinessmanagement.scheduledreport.application.dto;

import com.gogidix.globalbusinessmanagement.scheduledreport.application.dto.ScheduledReportRequestDto;
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
class ScheduledReportRequestDtoTest {

        @Test
    void testBuilder() {
        ScheduledReportRequestDto dto = ScheduledReportRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .reportId("test-reportId")
            .cronExpression("test-cronExpression")
            .recipients("test-recipients")
            .format("test-format")
            .isActive("test-isActive")
            .lastRunAt("test-lastRunAt")
            .build();
        assertNotNull(dto);
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
        ScheduledReportRequestDto dto = new ScheduledReportRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setReportId("val-reportId");
        dto.setCronExpression("val-cronExpression");
        dto.setRecipients("val-recipients");
        dto.setFormat("val-format");
        dto.setIsActive("val-isActive");
        dto.setLastRunAt("val-lastRunAt");
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
        ScheduledReportRequestDto dto1 = ScheduledReportRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .reportId("test-reportId")
            .cronExpression("test-cronExpression")
            .recipients("test-recipients")
            .format("test-format")
            .isActive("test-isActive")
            .lastRunAt("test-lastRunAt")
            .build();
        ScheduledReportRequestDto dto2 = ScheduledReportRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .reportId("test-reportId")
            .cronExpression("test-cronExpression")
            .recipients("test-recipients")
            .format("test-format")
            .isActive("test-isActive")
            .lastRunAt("test-lastRunAt")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ScheduledReportRequestDto dto = ScheduledReportRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .reportId("test-reportId")
            .cronExpression("test-cronExpression")
            .recipients("test-recipients")
            .format("test-format")
            .isActive("test-isActive")
            .lastRunAt("test-lastRunAt")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}