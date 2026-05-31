package com.gogidix.finance.forecasting.domain.port.in;

import com.gogidix.finance.forecasting.domain.port.in.ForecastCommand;
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
class ForecastCommand_UpdateForecastCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.UpdateForecastCommand dto = new ForecastCommand.UpdateForecastCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setDepartment("val-department");
        dto.setCategory("val-category");
        dto.setScenario("val-scenario");
        dto.setConfidenceLevel(99);
        dto.setDataSource("val-dataSource");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-scenario", dto.getScenario());
        assertEquals(99, dto.getConfidenceLevel());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.UpdateForecastCommand dto1 = new ForecastCommand.UpdateForecastCommand();
        ForecastCommand.UpdateForecastCommand dto2 = new ForecastCommand.UpdateForecastCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setDepartment("test");
        dto1.setCategory("test");
        dto1.setScenario("test");
        dto1.setConfidenceLevel(42);
        dto1.setDataSource("test");
        dto1.setNotes("test");
        dto1.setMetrics(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setDepartment("test");
        dto2.setCategory("test");
        dto2.setScenario("test");
        dto2.setConfidenceLevel(42);
        dto2.setDataSource("test");
        dto2.setNotes("test");
        dto2.setMetrics(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.UpdateForecastCommand dto = new ForecastCommand.UpdateForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setDepartment("test");
        dto.setCategory("test");
        dto.setScenario("test");
        dto.setConfidenceLevel(42);
        dto.setDataSource("test");
        dto.setNotes("test");
        dto.setMetrics(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.UpdateForecastCommand dto = new ForecastCommand.UpdateForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setDepartment("test");
        dto.setCategory("test");
        dto.setScenario("test");
        dto.setConfidenceLevel(42);
        dto.setDataSource("test");
        dto.setNotes("test");
        dto.setMetrics(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}