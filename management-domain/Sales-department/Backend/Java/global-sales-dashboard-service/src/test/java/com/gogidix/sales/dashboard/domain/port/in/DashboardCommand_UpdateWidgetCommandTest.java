package com.gogidix.sales.dashboard.domain.port.in;

import com.gogidix.sales.dashboard.domain.port.in.DashboardCommand;
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
class DashboardCommand_UpdateWidgetCommandTest {

        @Test
    void testSettersAndGetters() {
        DashboardCommand.UpdateWidgetCommand dto = new DashboardCommand.UpdateWidgetCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setWidgetId("val-widgetId");
        dto.setDisplayValue("val-displayValue");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-widgetId", dto.getWidgetId());
        assertEquals("val-displayValue", dto.getDisplayValue());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardCommand.UpdateWidgetCommand dto1 = new DashboardCommand.UpdateWidgetCommand();
        DashboardCommand.UpdateWidgetCommand dto2 = new DashboardCommand.UpdateWidgetCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setWidgetId("test");
        dto1.setValue(null);
        dto1.setDisplayValue("test");
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setWidgetId("test");
        dto2.setValue(null);
        dto2.setDisplayValue("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardCommand.UpdateWidgetCommand dto = new DashboardCommand.UpdateWidgetCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setWidgetId("test");
        dto.setValue(null);
        dto.setDisplayValue("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardCommand.UpdateWidgetCommand dto = new DashboardCommand.UpdateWidgetCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setWidgetId("test");
        dto.setValue(null);
        dto.setDisplayValue("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}