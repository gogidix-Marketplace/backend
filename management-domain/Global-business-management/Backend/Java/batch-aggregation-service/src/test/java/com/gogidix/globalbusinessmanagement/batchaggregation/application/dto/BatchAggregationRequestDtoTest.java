package com.gogidix.globalbusinessmanagement.batchaggregation.application.dto;

import com.gogidix.globalbusinessmanagement.batchaggregation.application.dto.BatchAggregationRequestDto;
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
class BatchAggregationRequestDtoTest {

        @Test
    void testBuilder() {
        BatchAggregationRequestDto dto = BatchAggregationRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .status("test-status")
            .schedule("test-schedule")
            .region("test-region")
            .country("test-country")
            .build();
        assertNotNull(dto);
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
        BatchAggregationRequestDto dto = new BatchAggregationRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setAggregationType("val-aggregationType");
        dto.setDataSource("val-dataSource");
        dto.setStatus("val-status");
        dto.setSchedule("val-schedule");
        dto.setRegion("val-region");
        dto.setCountry("val-country");
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
        BatchAggregationRequestDto dto1 = BatchAggregationRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .status("test-status")
            .schedule("test-schedule")
            .region("test-region")
            .country("test-country")
            .build();
        BatchAggregationRequestDto dto2 = BatchAggregationRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .status("test-status")
            .schedule("test-schedule")
            .region("test-region")
            .country("test-country")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BatchAggregationRequestDto dto = BatchAggregationRequestDto.builder()
                        .tenantId("test-tenantId")
            .name("test-name")
            .aggregationType("test-aggregationType")
            .dataSource("test-dataSource")
            .status("test-status")
            .schedule("test-schedule")
            .region("test-region")
            .country("test-country")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}