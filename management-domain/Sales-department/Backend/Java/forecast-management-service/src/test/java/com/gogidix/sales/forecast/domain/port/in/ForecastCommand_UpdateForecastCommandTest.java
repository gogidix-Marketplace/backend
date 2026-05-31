package com.gogidix.sales.forecast.domain.port.in;

import com.gogidix.sales.forecast.domain.port.in.ForecastCommand;
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
        dto.setRegion("val-region");
        dto.setTerritory("val-territory");
        dto.setBusinessUnit("val-businessUnit");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-businessUnit", dto.getBusinessUnit());
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
        dto1.setRegion("test");
        dto1.setTerritory("test");
        dto1.setBusinessUnit("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setRegion("test");
        dto2.setTerritory("test");
        dto2.setBusinessUnit("test");
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
        dto.setRegion("test");
        dto.setTerritory("test");
        dto.setBusinessUnit("test");
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
        dto.setRegion("test");
        dto.setTerritory("test");
        dto.setBusinessUnit("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}