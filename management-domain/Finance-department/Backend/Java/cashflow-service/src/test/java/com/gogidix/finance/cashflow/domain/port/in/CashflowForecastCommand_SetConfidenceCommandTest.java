package com.gogidix.finance.cashflow.domain.port.in;

import com.gogidix.finance.cashflow.domain.model.CashflowForecast;
import com.gogidix.finance.cashflow.domain.port.in.CashflowForecastCommand;
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
class CashflowForecastCommand_SetConfidenceCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastCommand.SetConfidenceCommand dto = new CashflowForecastCommand.SetConfidenceCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setVariancePercentage(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastCommand.SetConfidenceCommand dto1 = new CashflowForecastCommand.SetConfidenceCommand();
        CashflowForecastCommand.SetConfidenceCommand dto2 = new CashflowForecastCommand.SetConfidenceCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto1.setVariancePercentage(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto2.setVariancePercentage(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastCommand.SetConfidenceCommand dto = new CashflowForecastCommand.SetConfidenceCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto.setVariancePercentage(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastCommand.SetConfidenceCommand dto = new CashflowForecastCommand.SetConfidenceCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto.setVariancePercentage(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}