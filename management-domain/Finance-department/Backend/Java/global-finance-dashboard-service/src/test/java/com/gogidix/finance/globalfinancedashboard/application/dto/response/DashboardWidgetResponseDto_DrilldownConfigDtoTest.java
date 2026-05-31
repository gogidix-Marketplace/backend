package com.gogidix.finance.globalfinancedashboard.application.dto.response;

import com.gogidix.finance.globalfinancedashboard.application.dto.response.DashboardWidgetResponseDto;
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
class DashboardWidgetResponseDto_DrilldownConfigDtoTest {

        @Test
    void testBuilder() {
        DashboardWidgetResponseDto.DrilldownConfigDto dto = DashboardWidgetResponseDto.DrilldownConfigDto.builder()
                        .targetDashboard("test-targetDashboard")
            .targetWidget("test-targetWidget")
            .parameterMapping(Collections.emptyMap())
            .openInNewTab(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-targetDashboard", dto.getTargetDashboard());
        assertEquals("test-targetWidget", dto.getTargetWidget());
        assertTrue(dto.getOpenInNewTab());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidgetResponseDto.DrilldownConfigDto dto = new DashboardWidgetResponseDto.DrilldownConfigDto();
        dto.setTargetDashboard("val-targetDashboard");
        dto.setTargetWidget("val-targetWidget");
        dto.setOpenInNewTab(true);
        assertEquals("val-targetDashboard", dto.getTargetDashboard());
        assertEquals("val-targetWidget", dto.getTargetWidget());
        assertTrue(dto.getOpenInNewTab());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidgetResponseDto.DrilldownConfigDto dto1 = DashboardWidgetResponseDto.DrilldownConfigDto.builder()
                        .targetDashboard("test-targetDashboard")
            .targetWidget("test-targetWidget")
            .parameterMapping(Collections.emptyMap())
            .openInNewTab(true)
            .build();
        DashboardWidgetResponseDto.DrilldownConfigDto dto2 = DashboardWidgetResponseDto.DrilldownConfigDto.builder()
                        .targetDashboard("test-targetDashboard")
            .targetWidget("test-targetWidget")
            .parameterMapping(Collections.emptyMap())
            .openInNewTab(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidgetResponseDto.DrilldownConfigDto dto = DashboardWidgetResponseDto.DrilldownConfigDto.builder()
                        .targetDashboard("test-targetDashboard")
            .targetWidget("test-targetWidget")
            .parameterMapping(Collections.emptyMap())
            .openInNewTab(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}