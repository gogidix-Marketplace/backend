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
class KPIBoardDto_DisplayConfigDtoTest {

        @Test
    void testBuilder() {
        KPIBoardDto.DisplayConfigDto dto = KPIBoardDto.DisplayConfigDto.builder()
                        .visualizationType("test-visualizationType")
            .color("test-color")
            .width(42)
            .height(42)
            .showSparkline(true)
            .showTrend(true)
            .showComparison(true)
            .chartType("test-chartType")
            .build();
        assertNotNull(dto);
        assertEquals("test-visualizationType", dto.getVisualizationType());
        assertEquals("test-color", dto.getColor());
        assertEquals(42, dto.getWidth());
        assertEquals(42, dto.getHeight());
        assertTrue(dto.getShowSparkline());
        assertTrue(dto.getShowTrend());
        assertTrue(dto.getShowComparison());
        assertEquals("test-chartType", dto.getChartType());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoardDto.DisplayConfigDto dto = new KPIBoardDto.DisplayConfigDto();
        dto.setVisualizationType("val-visualizationType");
        dto.setColor("val-color");
        dto.setWidth(99);
        dto.setHeight(99);
        dto.setShowSparkline(true);
        dto.setShowTrend(true);
        dto.setShowComparison(true);
        dto.setChartType("val-chartType");
        assertEquals("val-visualizationType", dto.getVisualizationType());
        assertEquals("val-color", dto.getColor());
        assertEquals(99, dto.getWidth());
        assertEquals(99, dto.getHeight());
        assertTrue(dto.getShowSparkline());
        assertTrue(dto.getShowTrend());
        assertTrue(dto.getShowComparison());
        assertEquals("val-chartType", dto.getChartType());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoardDto.DisplayConfigDto dto1 = KPIBoardDto.DisplayConfigDto.builder()
                        .visualizationType("test-visualizationType")
            .color("test-color")
            .width(42)
            .height(42)
            .showSparkline(true)
            .showTrend(true)
            .showComparison(true)
            .chartType("test-chartType")
            .build();
        KPIBoardDto.DisplayConfigDto dto2 = KPIBoardDto.DisplayConfigDto.builder()
                        .visualizationType("test-visualizationType")
            .color("test-color")
            .width(42)
            .height(42)
            .showSparkline(true)
            .showTrend(true)
            .showComparison(true)
            .chartType("test-chartType")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoardDto.DisplayConfigDto dto = KPIBoardDto.DisplayConfigDto.builder()
                        .visualizationType("test-visualizationType")
            .color("test-color")
            .width(42)
            .height(42)
            .showSparkline(true)
            .showTrend(true)
            .showComparison(true)
            .chartType("test-chartType")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}