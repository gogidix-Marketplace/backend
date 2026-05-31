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
class ForecastCommand_AddMetricCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.AddMetricCommand dto = new ForecastCommand.AddMetricCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setAddedBy("val-addedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-addedBy", dto.getAddedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.AddMetricCommand dto1 = new ForecastCommand.AddMetricCommand();
        ForecastCommand.AddMetricCommand dto2 = new ForecastCommand.AddMetricCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setMetric(null);
        dto1.setAddedBy("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setMetric(null);
        dto2.setAddedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.AddMetricCommand dto = new ForecastCommand.AddMetricCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setMetric(null);
        dto.setAddedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.AddMetricCommand dto = new ForecastCommand.AddMetricCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setMetric(null);
        dto.setAddedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}