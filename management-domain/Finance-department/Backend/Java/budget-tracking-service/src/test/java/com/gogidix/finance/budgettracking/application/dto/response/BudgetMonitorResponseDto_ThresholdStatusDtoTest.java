package com.gogidix.finance.budgettracking.application.dto.response;

import com.gogidix.finance.budgettracking.application.dto.response.BudgetMonitorResponseDto;
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
class BudgetMonitorResponseDto_ThresholdStatusDtoTest {

        @Test
    void testBuilder() {
        BudgetMonitorResponseDto.ThresholdStatusDto dto = BudgetMonitorResponseDto.ThresholdStatusDto.builder()
                        .thresholdType("test-thresholdType")
            .thresholdValue(BigDecimal.TEN)
            .currentValue(BigDecimal.TEN)
            .level(null)
            .breached(true)
            .breachedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledged(true)
            .acknowledgedBy("test-acknowledgedBy")
            .acknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-thresholdType", dto.getThresholdType());
        assertEquals(BigDecimal.TEN, dto.getThresholdValue());
        assertEquals(BigDecimal.TEN, dto.getCurrentValue());
        assertTrue(dto.isBreached());
        assertTrue(dto.isAcknowledged());
        assertEquals("test-acknowledgedBy", dto.getAcknowledgedBy());
    }

    @Test
    void testSettersAndGetters() {
        BudgetMonitorResponseDto.ThresholdStatusDto dto = new BudgetMonitorResponseDto.ThresholdStatusDto();
        dto.setThresholdType("val-thresholdType");
        dto.setThresholdValue(BigDecimal.ONE);
        dto.setCurrentValue(BigDecimal.ONE);
        dto.setBreached(true);
        dto.setAcknowledged(true);
        dto.setAcknowledgedBy("val-acknowledgedBy");
        assertEquals("val-thresholdType", dto.getThresholdType());
        assertEquals(BigDecimal.ONE, dto.getThresholdValue());
        assertEquals(BigDecimal.ONE, dto.getCurrentValue());
        assertTrue(dto.isBreached());
        assertTrue(dto.isAcknowledged());
        assertEquals("val-acknowledgedBy", dto.getAcknowledgedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetMonitorResponseDto.ThresholdStatusDto dto1 = BudgetMonitorResponseDto.ThresholdStatusDto.builder()
                        .thresholdType("test-thresholdType")
            .thresholdValue(BigDecimal.TEN)
            .currentValue(BigDecimal.TEN)
            .level(null)
            .breached(true)
            .breachedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledged(true)
            .acknowledgedBy("test-acknowledgedBy")
            .acknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        BudgetMonitorResponseDto.ThresholdStatusDto dto2 = BudgetMonitorResponseDto.ThresholdStatusDto.builder()
                        .thresholdType("test-thresholdType")
            .thresholdValue(BigDecimal.TEN)
            .currentValue(BigDecimal.TEN)
            .level(null)
            .breached(true)
            .breachedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledged(true)
            .acknowledgedBy("test-acknowledgedBy")
            .acknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BudgetMonitorResponseDto.ThresholdStatusDto dto = BudgetMonitorResponseDto.ThresholdStatusDto.builder()
                        .thresholdType("test-thresholdType")
            .thresholdValue(BigDecimal.TEN)
            .currentValue(BigDecimal.TEN)
            .level(null)
            .breached(true)
            .breachedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .acknowledged(true)
            .acknowledgedBy("test-acknowledgedBy")
            .acknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}