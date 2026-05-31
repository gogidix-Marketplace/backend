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
class CashflowForecastCommand_ApproveForecastCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastCommand.ApproveForecastCommand dto = new CashflowForecastCommand.ApproveForecastCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setApprovedBy("val-approvedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastCommand.ApproveForecastCommand dto1 = new CashflowForecastCommand.ApproveForecastCommand();
        CashflowForecastCommand.ApproveForecastCommand dto2 = new CashflowForecastCommand.ApproveForecastCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setApprovedBy("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setApprovedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastCommand.ApproveForecastCommand dto = new CashflowForecastCommand.ApproveForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setApprovedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastCommand.ApproveForecastCommand dto = new CashflowForecastCommand.ApproveForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setApprovedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}