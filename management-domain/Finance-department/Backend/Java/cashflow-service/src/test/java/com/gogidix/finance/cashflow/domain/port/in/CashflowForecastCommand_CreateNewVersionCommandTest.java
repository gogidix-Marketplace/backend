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
class CashflowForecastCommand_CreateNewVersionCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastCommand.CreateNewVersionCommand dto = new CashflowForecastCommand.CreateNewVersionCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setGeneratedBy("val-generatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-generatedBy", dto.getGeneratedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastCommand.CreateNewVersionCommand dto1 = new CashflowForecastCommand.CreateNewVersionCommand();
        CashflowForecastCommand.CreateNewVersionCommand dto2 = new CashflowForecastCommand.CreateNewVersionCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setGeneratedBy("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setGeneratedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastCommand.CreateNewVersionCommand dto = new CashflowForecastCommand.CreateNewVersionCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setGeneratedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastCommand.CreateNewVersionCommand dto = new CashflowForecastCommand.CreateNewVersionCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setGeneratedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}