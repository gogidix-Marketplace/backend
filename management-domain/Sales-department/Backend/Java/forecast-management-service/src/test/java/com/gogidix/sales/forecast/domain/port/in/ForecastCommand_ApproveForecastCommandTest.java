package com.gogidix.sales.forecast.domain.port.in;

import com.gogidix.sales.forecast.domain.model.Forecast;
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
class ForecastCommand_ApproveForecastCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.ApproveForecastCommand dto = new ForecastCommand.ApproveForecastCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setApprover("val-approver");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-approver", dto.getApprover());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.ApproveForecastCommand dto1 = new ForecastCommand.ApproveForecastCommand();
        ForecastCommand.ApproveForecastCommand dto2 = new ForecastCommand.ApproveForecastCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setApprover("test");
        dto1.setApprovalLevel(Forecast.ApprovalLevel.NONE);
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setApprover("test");
        dto2.setApprovalLevel(Forecast.ApprovalLevel.NONE);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.ApproveForecastCommand dto = new ForecastCommand.ApproveForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setApprover("test");
        dto.setApprovalLevel(Forecast.ApprovalLevel.NONE);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.ApproveForecastCommand dto = new ForecastCommand.ApproveForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setApprover("test");
        dto.setApprovalLevel(Forecast.ApprovalLevel.NONE);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}