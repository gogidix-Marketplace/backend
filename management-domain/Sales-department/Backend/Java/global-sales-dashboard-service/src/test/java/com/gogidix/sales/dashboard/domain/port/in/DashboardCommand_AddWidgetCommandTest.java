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
class DashboardCommand_AddWidgetCommandTest {

        @Test
    void testSettersAndGetters() {
        DashboardCommand.AddWidgetCommand dto = new DashboardCommand.AddWidgetCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setRow(99);
        dto.setColumn(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardCommand.AddWidgetCommand dto1 = new DashboardCommand.AddWidgetCommand();
        DashboardCommand.AddWidgetCommand dto2 = new DashboardCommand.AddWidgetCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setTitle("test");
        dto1.setWidgetType(null);
        dto1.setCategory(null);
        dto1.setDescription("test");
        dto1.setInitialValue(null);
        dto1.setRow(42);
        dto1.setColumn(42);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setTitle("test");
        dto2.setWidgetType(null);
        dto2.setCategory(null);
        dto2.setDescription("test");
        dto2.setInitialValue(null);
        dto2.setRow(42);
        dto2.setColumn(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardCommand.AddWidgetCommand dto = new DashboardCommand.AddWidgetCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setTitle("test");
        dto.setWidgetType(null);
        dto.setCategory(null);
        dto.setDescription("test");
        dto.setInitialValue(null);
        dto.setRow(42);
        dto.setColumn(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardCommand.AddWidgetCommand dto = new DashboardCommand.AddWidgetCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setTitle("test");
        dto.setWidgetType(null);
        dto.setCategory(null);
        dto.setDescription("test");
        dto.setInitialValue(null);
        dto.setRow(42);
        dto.setColumn(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}