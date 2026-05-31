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
class ForecastCommand_UpdateActualAmountCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.UpdateActualAmountCommand dto = new ForecastCommand.UpdateActualAmountCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setActualAmount(BigDecimal.ONE);
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.UpdateActualAmountCommand dto1 = new ForecastCommand.UpdateActualAmountCommand();
        ForecastCommand.UpdateActualAmountCommand dto2 = new ForecastCommand.UpdateActualAmountCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setActualAmount(BigDecimal.TEN);
        dto1.setUpdatedBy("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setActualAmount(BigDecimal.TEN);
        dto2.setUpdatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.UpdateActualAmountCommand dto = new ForecastCommand.UpdateActualAmountCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setActualAmount(BigDecimal.TEN);
        dto.setUpdatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.UpdateActualAmountCommand dto = new ForecastCommand.UpdateActualAmountCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setActualAmount(BigDecimal.TEN);
        dto.setUpdatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}