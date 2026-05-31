package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_DrillDownConfigDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.DrillDownConfigDto dto = DashboardResponseDto.DrillDownConfigDto.builder()
                        .id("test-id")
            .name("test-name")
            .targetDashboardId("test-targetDashboardId")
            .filters(Collections.emptyList())
            .type("test-type")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-targetDashboardId", dto.getTargetDashboardId());
        assertEquals("test-type", dto.getType());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.DrillDownConfigDto dto = new DashboardResponseDto.DrillDownConfigDto();
        dto.setId("val-id");
        dto.setName("val-name");
        dto.setTargetDashboardId("val-targetDashboardId");
        dto.setType("val-type");
        assertEquals("val-id", dto.getId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-targetDashboardId", dto.getTargetDashboardId());
        assertEquals("val-type", dto.getType());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.DrillDownConfigDto dto1 = DashboardResponseDto.DrillDownConfigDto.builder()
                        .id("test-id")
            .name("test-name")
            .targetDashboardId("test-targetDashboardId")
            .filters(Collections.emptyList())
            .type("test-type")
            .build();
        DashboardResponseDto.DrillDownConfigDto dto2 = DashboardResponseDto.DrillDownConfigDto.builder()
                        .id("test-id")
            .name("test-name")
            .targetDashboardId("test-targetDashboardId")
            .filters(Collections.emptyList())
            .type("test-type")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.DrillDownConfigDto dto = DashboardResponseDto.DrillDownConfigDto.builder()
                        .id("test-id")
            .name("test-name")
            .targetDashboardId("test-targetDashboardId")
            .filters(Collections.emptyList())
            .type("test-type")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}