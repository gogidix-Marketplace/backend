package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.KPIBoardDto;
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
class KPIBoardDto_ThresholdConfigDtoTest {

        @Test
    void testBuilder() {
        KPIBoardDto.ThresholdConfigDto dto = KPIBoardDto.ThresholdConfigDto.builder()
                        .type("test-type")
            .warningThreshold(BigDecimal.TEN)
            .criticalThreshold(BigDecimal.TEN)
            .targetThreshold(BigDecimal.TEN)
            .warningColor("test-warningColor")
            .criticalColor("test-criticalColor")
            .targetColor("test-targetColor")
            .build();
        assertNotNull(dto);
        assertEquals("test-type", dto.getType());
        assertEquals(BigDecimal.TEN, dto.getWarningThreshold());
        assertEquals(BigDecimal.TEN, dto.getCriticalThreshold());
        assertEquals(BigDecimal.TEN, dto.getTargetThreshold());
        assertEquals("test-warningColor", dto.getWarningColor());
        assertEquals("test-criticalColor", dto.getCriticalColor());
        assertEquals("test-targetColor", dto.getTargetColor());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoardDto.ThresholdConfigDto dto = new KPIBoardDto.ThresholdConfigDto();
        dto.setType("val-type");
        dto.setWarningThreshold(BigDecimal.ONE);
        dto.setCriticalThreshold(BigDecimal.ONE);
        dto.setTargetThreshold(BigDecimal.ONE);
        dto.setWarningColor("val-warningColor");
        dto.setCriticalColor("val-criticalColor");
        dto.setTargetColor("val-targetColor");
        assertEquals("val-type", dto.getType());
        assertEquals(BigDecimal.ONE, dto.getWarningThreshold());
        assertEquals(BigDecimal.ONE, dto.getCriticalThreshold());
        assertEquals(BigDecimal.ONE, dto.getTargetThreshold());
        assertEquals("val-warningColor", dto.getWarningColor());
        assertEquals("val-criticalColor", dto.getCriticalColor());
        assertEquals("val-targetColor", dto.getTargetColor());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoardDto.ThresholdConfigDto dto1 = KPIBoardDto.ThresholdConfigDto.builder()
                        .type("test-type")
            .warningThreshold(BigDecimal.TEN)
            .criticalThreshold(BigDecimal.TEN)
            .targetThreshold(BigDecimal.TEN)
            .warningColor("test-warningColor")
            .criticalColor("test-criticalColor")
            .targetColor("test-targetColor")
            .build();
        KPIBoardDto.ThresholdConfigDto dto2 = KPIBoardDto.ThresholdConfigDto.builder()
                        .type("test-type")
            .warningThreshold(BigDecimal.TEN)
            .criticalThreshold(BigDecimal.TEN)
            .targetThreshold(BigDecimal.TEN)
            .warningColor("test-warningColor")
            .criticalColor("test-criticalColor")
            .targetColor("test-targetColor")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoardDto.ThresholdConfigDto dto = KPIBoardDto.ThresholdConfigDto.builder()
                        .type("test-type")
            .warningThreshold(BigDecimal.TEN)
            .criticalThreshold(BigDecimal.TEN)
            .targetThreshold(BigDecimal.TEN)
            .warningColor("test-warningColor")
            .criticalColor("test-criticalColor")
            .targetColor("test-targetColor")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}