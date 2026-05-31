package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.RetentionResponseDto;
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
class RetentionResponseDto_DepartureReasonDtoTest {

        @Test
    void testBuilder() {
        RetentionResponseDto.DepartureReasonDto dto = RetentionResponseDto.DepartureReasonDto.builder()
                        .reason("test-reason")
            .category("test-category")
            .count(42)
            .percentage(null)
            .preventable(true)
            .trend("test-trend")
            .build();
        assertNotNull(dto);
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-category", dto.getCategory());
        assertEquals(42, dto.getCount());
        assertTrue(dto.isPreventable());
        assertEquals("test-trend", dto.getTrend());
    }

    @Test
    void testSettersAndGetters() {
        RetentionResponseDto.DepartureReasonDto dto = new RetentionResponseDto.DepartureReasonDto();
        dto.setReason("val-reason");
        dto.setCategory("val-category");
        dto.setCount(99);
        dto.setPreventable(true);
        dto.setTrend("val-trend");
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-category", dto.getCategory());
        assertEquals(99, dto.getCount());
        assertTrue(dto.isPreventable());
        assertEquals("val-trend", dto.getTrend());
    }

    @Test
    void testEqualsAndHashCode() {
        RetentionResponseDto.DepartureReasonDto dto1 = RetentionResponseDto.DepartureReasonDto.builder()
                        .reason("test-reason")
            .category("test-category")
            .count(42)
            .percentage(null)
            .preventable(true)
            .trend("test-trend")
            .build();
        RetentionResponseDto.DepartureReasonDto dto2 = RetentionResponseDto.DepartureReasonDto.builder()
                        .reason("test-reason")
            .category("test-category")
            .count(42)
            .percentage(null)
            .preventable(true)
            .trend("test-trend")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RetentionResponseDto.DepartureReasonDto dto = RetentionResponseDto.DepartureReasonDto.builder()
                        .reason("test-reason")
            .category("test-category")
            .count(42)
            .percentage(null)
            .preventable(true)
            .trend("test-trend")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}