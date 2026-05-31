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
class DashboardWidgetResponseDto_CalculationDtoTest {

        @Test
    void testBuilder() {
        DashboardWidgetResponseDto.CalculationDto dto = DashboardWidgetResponseDto.CalculationDto.builder()
                        .name("test-name")
            .formula("test-formula")
            .dependencies(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-formula", dto.getFormula());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidgetResponseDto.CalculationDto dto = new DashboardWidgetResponseDto.CalculationDto();
        dto.setName("val-name");
        dto.setFormula("val-formula");
        assertEquals("val-name", dto.getName());
        assertEquals("val-formula", dto.getFormula());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidgetResponseDto.CalculationDto dto1 = DashboardWidgetResponseDto.CalculationDto.builder()
                        .name("test-name")
            .formula("test-formula")
            .dependencies(Collections.emptyList())
            .build();
        DashboardWidgetResponseDto.CalculationDto dto2 = DashboardWidgetResponseDto.CalculationDto.builder()
                        .name("test-name")
            .formula("test-formula")
            .dependencies(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidgetResponseDto.CalculationDto dto = DashboardWidgetResponseDto.CalculationDto.builder()
                        .name("test-name")
            .formula("test-formula")
            .dependencies(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}