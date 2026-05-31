package com.gogidix.finance.budgettracking.domain.model;

import com.gogidix.finance.budgettracking.domain.model.ThresholdAlert;
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
class ThresholdAlert_AlertHistoryTest {

        @Test
    void testBuilder() {
        ThresholdAlert.AlertHistory dto = ThresholdAlert.AlertHistory.builder()
                        .triggeredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .currentValue(BigDecimal.TEN)
            .triggeredBy("test-triggeredBy")
            .message("test-message")
            .acknowledged(true)
            .acknowledgedBy("test-acknowledgedBy")
            .acknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCurrentValue());
        assertEquals("test-triggeredBy", dto.getTriggeredBy());
        assertEquals("test-message", dto.getMessage());
        assertTrue(dto.isAcknowledged());
        assertEquals("test-acknowledgedBy", dto.getAcknowledgedBy());
    }

    @Test
    void testSettersAndGetters() {
        ThresholdAlert.AlertHistory dto = new ThresholdAlert.AlertHistory();
        dto.setCurrentValue(BigDecimal.ONE);
        dto.setTriggeredBy("val-triggeredBy");
        dto.setMessage("val-message");
        dto.setAcknowledged(true);
        dto.setAcknowledgedBy("val-acknowledgedBy");
        assertEquals(BigDecimal.ONE, dto.getCurrentValue());
        assertEquals("val-triggeredBy", dto.getTriggeredBy());
        assertEquals("val-message", dto.getMessage());
        assertTrue(dto.isAcknowledged());
        assertEquals("val-acknowledgedBy", dto.getAcknowledgedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ThresholdAlert.AlertHistory dto1 = ThresholdAlert.AlertHistory.builder()
                        .triggeredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .currentValue(BigDecimal.TEN)
            .triggeredBy("test-triggeredBy")
            .message("test-message")
            .acknowledged(true)
            .acknowledgedBy("test-acknowledgedBy")
            .acknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ThresholdAlert.AlertHistory dto2 = ThresholdAlert.AlertHistory.builder()
                        .triggeredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .currentValue(BigDecimal.TEN)
            .triggeredBy("test-triggeredBy")
            .message("test-message")
            .acknowledged(true)
            .acknowledgedBy("test-acknowledgedBy")
            .acknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ThresholdAlert.AlertHistory dto = ThresholdAlert.AlertHistory.builder()
                        .triggeredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .currentValue(BigDecimal.TEN)
            .triggeredBy("test-triggeredBy")
            .message("test-message")
            .acknowledged(true)
            .acknowledgedBy("test-acknowledgedBy")
            .acknowledgedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}