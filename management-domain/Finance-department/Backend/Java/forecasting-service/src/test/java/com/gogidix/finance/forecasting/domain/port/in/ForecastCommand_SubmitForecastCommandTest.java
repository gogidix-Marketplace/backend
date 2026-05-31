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
class ForecastCommand_SubmitForecastCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.SubmitForecastCommand dto = new ForecastCommand.SubmitForecastCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setSubmittedBy("val-submittedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-submittedBy", dto.getSubmittedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.SubmitForecastCommand dto1 = new ForecastCommand.SubmitForecastCommand();
        ForecastCommand.SubmitForecastCommand dto2 = new ForecastCommand.SubmitForecastCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setSubmittedBy("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setSubmittedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.SubmitForecastCommand dto = new ForecastCommand.SubmitForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setSubmittedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.SubmitForecastCommand dto = new ForecastCommand.SubmitForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setSubmittedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}