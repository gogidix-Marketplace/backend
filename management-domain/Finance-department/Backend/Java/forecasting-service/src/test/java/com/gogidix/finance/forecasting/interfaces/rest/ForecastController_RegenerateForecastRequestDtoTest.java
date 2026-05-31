package com.gogidix.finance.forecasting.interfaces.rest;

import com.gogidix.finance.forecasting.interfaces.rest.ForecastController;
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
class ForecastController_RegenerateForecastRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ForecastController.RegenerateForecastRequestDto dto = new ForecastController.RegenerateForecastRequestDto();
        dto.setNewTotalAmount(BigDecimal.ONE);
        dto.setDataSource("val-dataSource");
        dto.setNewConfidenceLevel(99);
        dto.setScenario("val-scenario");
        assertEquals(BigDecimal.ONE, dto.getNewTotalAmount());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals(99, dto.getNewConfidenceLevel());
        assertEquals("val-scenario", dto.getScenario());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastController.RegenerateForecastRequestDto dto1 = new ForecastController.RegenerateForecastRequestDto();
        ForecastController.RegenerateForecastRequestDto dto2 = new ForecastController.RegenerateForecastRequestDto();
        dto1.setNewMetrics(Collections.emptyList());
        dto1.setNewTotalAmount(BigDecimal.TEN);
        dto1.setDataSource("test");
        dto1.setNewConfidenceLevel(42);
        dto1.setScenario("test");
        dto2.setNewMetrics(Collections.emptyList());
        dto2.setNewTotalAmount(BigDecimal.TEN);
        dto2.setDataSource("test");
        dto2.setNewConfidenceLevel(42);
        dto2.setScenario("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNewTotalAmount(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastController.RegenerateForecastRequestDto dto = new ForecastController.RegenerateForecastRequestDto();
        dto.setNewMetrics(Collections.emptyList());
        dto.setNewTotalAmount(BigDecimal.TEN);
        dto.setDataSource("test");
        dto.setNewConfidenceLevel(42);
        dto.setScenario("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastController.RegenerateForecastRequestDto dto = new ForecastController.RegenerateForecastRequestDto();
        dto.setNewMetrics(Collections.emptyList());
        dto.setNewTotalAmount(BigDecimal.TEN);
        dto.setDataSource("test");
        dto.setNewConfidenceLevel(42);
        dto.setScenario("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}