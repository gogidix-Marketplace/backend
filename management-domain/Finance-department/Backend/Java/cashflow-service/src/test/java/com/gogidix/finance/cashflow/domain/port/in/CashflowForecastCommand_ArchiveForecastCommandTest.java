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
class CashflowForecastCommand_ArchiveForecastCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastCommand.ArchiveForecastCommand dto = new CashflowForecastCommand.ArchiveForecastCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastCommand.ArchiveForecastCommand dto1 = new CashflowForecastCommand.ArchiveForecastCommand();
        CashflowForecastCommand.ArchiveForecastCommand dto2 = new CashflowForecastCommand.ArchiveForecastCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastCommand.ArchiveForecastCommand dto = new CashflowForecastCommand.ArchiveForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastCommand.ArchiveForecastCommand dto = new CashflowForecastCommand.ArchiveForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}