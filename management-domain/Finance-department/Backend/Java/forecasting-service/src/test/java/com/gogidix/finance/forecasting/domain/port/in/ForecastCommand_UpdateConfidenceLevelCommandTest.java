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
class ForecastCommand_UpdateConfidenceLevelCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.UpdateConfidenceLevelCommand dto = new ForecastCommand.UpdateConfidenceLevelCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setConfidenceLevel(99);
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals(99, dto.getConfidenceLevel());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.UpdateConfidenceLevelCommand dto1 = new ForecastCommand.UpdateConfidenceLevelCommand();
        ForecastCommand.UpdateConfidenceLevelCommand dto2 = new ForecastCommand.UpdateConfidenceLevelCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setConfidenceLevel(42);
        dto1.setUpdatedBy("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setConfidenceLevel(42);
        dto2.setUpdatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.UpdateConfidenceLevelCommand dto = new ForecastCommand.UpdateConfidenceLevelCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setConfidenceLevel(42);
        dto.setUpdatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.UpdateConfidenceLevelCommand dto = new ForecastCommand.UpdateConfidenceLevelCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setConfidenceLevel(42);
        dto.setUpdatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}