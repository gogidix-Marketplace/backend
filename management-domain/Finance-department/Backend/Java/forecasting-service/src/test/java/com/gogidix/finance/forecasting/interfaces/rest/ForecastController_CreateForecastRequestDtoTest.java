package com.gogidix.finance.forecasting.interfaces.rest;

import com.gogidix.finance.forecasting.domain.model.Forecast;
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
class ForecastController_CreateForecastRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ForecastController.CreateForecastRequestDto dto = new ForecastController.CreateForecastRequestDto();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setCurrency("val-currency");
        dto.setDepartment("val-department");
        dto.setCategory("val-category");
        dto.setScenario("val-scenario");
        dto.setConfidenceLevel(99);
        dto.setDataSource("val-dataSource");
        dto.setNotes("val-notes");
        dto.setInitialAmount(BigDecimal.ONE);
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-scenario", dto.getScenario());
        assertEquals(99, dto.getConfidenceLevel());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(BigDecimal.ONE, dto.getInitialAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastController.CreateForecastRequestDto dto1 = new ForecastController.CreateForecastRequestDto();
        ForecastController.CreateForecastRequestDto dto2 = new ForecastController.CreateForecastRequestDto();
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setForecastType(Forecast.ForecastType.REVENUE);
        dto1.setForecastHorizon(Forecast.ForecastHorizon.MONTHLY);
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setCurrency("test");
        dto1.setDepartment("test");
        dto1.setCategory("test");
        dto1.setScenario("test");
        dto1.setConfidenceLevel(42);
        dto1.setDataSource("test");
        dto1.setMetrics(Collections.emptyList());
        dto1.setNotes("test");
        dto1.setInitialAmount(BigDecimal.TEN);
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setForecastType(Forecast.ForecastType.REVENUE);
        dto2.setForecastHorizon(Forecast.ForecastHorizon.MONTHLY);
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setCurrency("test");
        dto2.setDepartment("test");
        dto2.setCategory("test");
        dto2.setScenario("test");
        dto2.setConfidenceLevel(42);
        dto2.setDataSource("test");
        dto2.setMetrics(Collections.emptyList());
        dto2.setNotes("test");
        dto2.setInitialAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastController.CreateForecastRequestDto dto = new ForecastController.CreateForecastRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setForecastHorizon(Forecast.ForecastHorizon.MONTHLY);
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setCurrency("test");
        dto.setDepartment("test");
        dto.setCategory("test");
        dto.setScenario("test");
        dto.setConfidenceLevel(42);
        dto.setDataSource("test");
        dto.setMetrics(Collections.emptyList());
        dto.setNotes("test");
        dto.setInitialAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastController.CreateForecastRequestDto dto = new ForecastController.CreateForecastRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setForecastType(Forecast.ForecastType.REVENUE);
        dto.setForecastHorizon(Forecast.ForecastHorizon.MONTHLY);
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setCurrency("test");
        dto.setDepartment("test");
        dto.setCategory("test");
        dto.setScenario("test");
        dto.setConfidenceLevel(42);
        dto.setDataSource("test");
        dto.setMetrics(Collections.emptyList());
        dto.setNotes("test");
        dto.setInitialAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}