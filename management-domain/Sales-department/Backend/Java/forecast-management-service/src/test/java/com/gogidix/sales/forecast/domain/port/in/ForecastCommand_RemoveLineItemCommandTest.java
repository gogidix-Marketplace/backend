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
class ForecastCommand_RemoveLineItemCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.RemoveLineItemCommand dto = new ForecastCommand.RemoveLineItemCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setLineItemId("val-lineItemId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-lineItemId", dto.getLineItemId());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.RemoveLineItemCommand dto1 = new ForecastCommand.RemoveLineItemCommand();
        ForecastCommand.RemoveLineItemCommand dto2 = new ForecastCommand.RemoveLineItemCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setLineItemId("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setLineItemId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.RemoveLineItemCommand dto = new ForecastCommand.RemoveLineItemCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setLineItemId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.RemoveLineItemCommand dto = new ForecastCommand.RemoveLineItemCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setLineItemId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}