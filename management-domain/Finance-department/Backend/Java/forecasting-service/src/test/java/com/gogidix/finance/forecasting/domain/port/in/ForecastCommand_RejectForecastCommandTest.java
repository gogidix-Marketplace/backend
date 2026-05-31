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
class ForecastCommand_RejectForecastCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.RejectForecastCommand dto = new ForecastCommand.RejectForecastCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setRejecter("val-rejecter");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-rejecter", dto.getRejecter());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.RejectForecastCommand dto1 = new ForecastCommand.RejectForecastCommand();
        ForecastCommand.RejectForecastCommand dto2 = new ForecastCommand.RejectForecastCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setRejecter("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setRejecter("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.RejectForecastCommand dto = new ForecastCommand.RejectForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setRejecter("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.RejectForecastCommand dto = new ForecastCommand.RejectForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setRejecter("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}