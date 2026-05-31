package com.gogidix.finance.cashflow.domain.port.in;

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
class CashflowForecastCommand_CalculateVarianceCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastCommand.CalculateVarianceCommand dto = new CashflowForecastCommand.CalculateVarianceCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setCategory("val-category");
        dto.setForecastedAmount(BigDecimal.ONE);
        dto.setActualAmount(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-category", dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getForecastedAmount());
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastCommand.CalculateVarianceCommand dto1 = new CashflowForecastCommand.CalculateVarianceCommand();
        CashflowForecastCommand.CalculateVarianceCommand dto2 = new CashflowForecastCommand.CalculateVarianceCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setCategory("test");
        dto1.setForecastedAmount(BigDecimal.TEN);
        dto1.setActualAmount(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setCategory("test");
        dto2.setForecastedAmount(BigDecimal.TEN);
        dto2.setActualAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastCommand.CalculateVarianceCommand dto = new CashflowForecastCommand.CalculateVarianceCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setCategory("test");
        dto.setForecastedAmount(BigDecimal.TEN);
        dto.setActualAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastCommand.CalculateVarianceCommand dto = new CashflowForecastCommand.CalculateVarianceCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setCategory("test");
        dto.setForecastedAmount(BigDecimal.TEN);
        dto.setActualAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}