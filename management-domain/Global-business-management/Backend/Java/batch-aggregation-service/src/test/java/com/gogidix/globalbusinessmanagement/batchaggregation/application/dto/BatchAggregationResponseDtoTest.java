package com.gogidix.globalbusinessmanagement.batchaggregation.application.dto;

import com.gogidix.globalbusinessmanagement.batchaggregation.application.dto.BatchAggregationResponseDto;
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
class BatchAggregationResponseDtoTest {

        @Test
    void testBuilder() {
        BatchAggregationResponseDto dto = BatchAggregationResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .status("test-status")
            .schedule("test-schedule")
            .region("test-region")
            .country("test-country")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-aggregationType", dto.getAggregationType());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-schedule", dto.getSchedule());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-country", dto.getCountry());
    }

    @Test
    void testSettersAndGetters() {
        BatchAggregationResponseDto dto = new BatchAggregationResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setAggregationType("val-aggregationType");
        dto.setDataSource("val-dataSource");
        dto.setStatus("val-status");
        dto.setSchedule("val-schedule");
        dto.setRegion("val-region");
        dto.setCountry("val-country");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-aggregationType", dto.getAggregationType());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-schedule", dto.getSchedule());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-country", dto.getCountry());
    }

    @Test
    void testEqualsAndHashCode() {
        BatchAggregationResponseDto dto1 = BatchAggregationResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .status("test-status")
            .schedule("test-schedule")
            .region("test-region")
            .country("test-country")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BatchAggregationResponseDto dto2 = BatchAggregationResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .status("test-status")
            .schedule("test-schedule")
            .region("test-region")
            .country("test-country")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BatchAggregationResponseDto dto = BatchAggregationResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .name("test-name")
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .status("test-status")
            .schedule("test-schedule")
            .region("test-region")
            .country("test-country")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}