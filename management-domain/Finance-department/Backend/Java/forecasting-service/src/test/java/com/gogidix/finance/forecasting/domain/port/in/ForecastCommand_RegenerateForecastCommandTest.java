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
class ForecastCommand_RegenerateForecastCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.RegenerateForecastCommand dto = new ForecastCommand.RegenerateForecastCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setRegeneratedBy("val-regeneratedBy");
        dto.setNewTotalAmount(BigDecimal.ONE);
        dto.setDataSource("val-dataSource");
        dto.setNewConfidenceLevel(99);
        dto.setScenario("val-scenario");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-regeneratedBy", dto.getRegeneratedBy());
        assertEquals(BigDecimal.ONE, dto.getNewTotalAmount());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals(99, dto.getNewConfidenceLevel());
        assertEquals("val-scenario", dto.getScenario());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.RegenerateForecastCommand dto1 = new ForecastCommand.RegenerateForecastCommand();
        ForecastCommand.RegenerateForecastCommand dto2 = new ForecastCommand.RegenerateForecastCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setRegeneratedBy("test");
        dto1.setNewMetrics(Collections.emptyList());
        dto1.setNewTotalAmount(BigDecimal.TEN);
        dto1.setDataSource("test");
        dto1.setNewConfidenceLevel(42);
        dto1.setScenario("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setRegeneratedBy("test");
        dto2.setNewMetrics(Collections.emptyList());
        dto2.setNewTotalAmount(BigDecimal.TEN);
        dto2.setDataSource("test");
        dto2.setNewConfidenceLevel(42);
        dto2.setScenario("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.RegenerateForecastCommand dto = new ForecastCommand.RegenerateForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setRegeneratedBy("test");
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
        ForecastCommand.RegenerateForecastCommand dto = new ForecastCommand.RegenerateForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setRegeneratedBy("test");
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