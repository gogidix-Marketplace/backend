package com.gogidix.globalbusinessmanagement.reportbuilder.application.dto;

import com.gogidix.globalbusinessmanagement.reportbuilder.application.dto.ReportRequestDto;
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
class ReportRequestDtoTest {

        @Test
    void testBuilder() {
        ReportRequestDto dto = ReportRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .reportType("test-reportType")
            .dataSource("test-dataSource")
            .format("test-format")
            .status("test-status")
            .schedule("test-schedule")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-reportType", dto.getReportType());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals("test-format", dto.getFormat());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-schedule", dto.getSchedule());
    }

    @Test
    void testSettersAndGetters() {
        ReportRequestDto dto = new ReportRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setReportType("val-reportType");
        dto.setDataSource("val-dataSource");
        dto.setFormat("val-format");
        dto.setStatus("val-status");
        dto.setSchedule("val-schedule");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-reportType", dto.getReportType());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals("val-format", dto.getFormat());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-schedule", dto.getSchedule());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportRequestDto dto1 = ReportRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .reportType("test-reportType")
            .dataSource("test-dataSource")
            .format("test-format")
            .status("test-status")
            .schedule("test-schedule")
            .build();
        ReportRequestDto dto2 = ReportRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .reportType("test-reportType")
            .dataSource("test-dataSource")
            .format("test-format")
            .status("test-status")
            .schedule("test-schedule")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ReportRequestDto dto = ReportRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .reportType("test-reportType")
            .dataSource("test-dataSource")
            .format("test-format")
            .status("test-status")
            .schedule("test-schedule")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}