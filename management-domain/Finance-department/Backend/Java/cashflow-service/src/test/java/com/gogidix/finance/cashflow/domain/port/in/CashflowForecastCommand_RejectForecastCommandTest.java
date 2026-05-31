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
class CashflowForecastCommand_RejectForecastCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastCommand.RejectForecastCommand dto = new CashflowForecastCommand.RejectForecastCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setRejectedBy("val-rejectedBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-rejectedBy", dto.getRejectedBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastCommand.RejectForecastCommand dto1 = new CashflowForecastCommand.RejectForecastCommand();
        CashflowForecastCommand.RejectForecastCommand dto2 = new CashflowForecastCommand.RejectForecastCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setRejectedBy("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setRejectedBy("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastCommand.RejectForecastCommand dto = new CashflowForecastCommand.RejectForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setRejectedBy("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastCommand.RejectForecastCommand dto = new CashflowForecastCommand.RejectForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setRejectedBy("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}