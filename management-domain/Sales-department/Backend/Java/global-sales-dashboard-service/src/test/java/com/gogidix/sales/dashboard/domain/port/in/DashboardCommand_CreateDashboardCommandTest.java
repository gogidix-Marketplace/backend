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
class DashboardCommand_CreateDashboardCommandTest {

        @Test
    void testSettersAndGetters() {
        DashboardCommand.CreateDashboardCommand dto = new DashboardCommand.CreateDashboardCommand();
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setRefreshIntervalMinutes(99);
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals(99, dto.getRefreshIntervalMinutes());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardCommand.CreateDashboardCommand dto1 = new DashboardCommand.CreateDashboardCommand();
        DashboardCommand.CreateDashboardCommand dto2 = new DashboardCommand.CreateDashboardCommand();
        dto1.setTenantId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setType(null);
        dto1.setBaseCurrency("test");
        dto1.setEnabledRegions(Collections.emptyList());
        dto1.setRefreshIntervalMinutes(42);
        dto1.setCreatedBy("test");
        dto2.setTenantId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setType(null);
        dto2.setBaseCurrency("test");
        dto2.setEnabledRegions(Collections.emptyList());
        dto2.setRefreshIntervalMinutes(42);
        dto2.setCreatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardCommand.CreateDashboardCommand dto = new DashboardCommand.CreateDashboardCommand();
        dto.setTenantId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setType(null);
        dto.setBaseCurrency("test");
        dto.setEnabledRegions(Collections.emptyList());
        dto.setRefreshIntervalMinutes(42);
        dto.setCreatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardCommand.CreateDashboardCommand dto = new DashboardCommand.CreateDashboardCommand();
        dto.setTenantId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setType(null);
        dto.setBaseCurrency("test");
        dto.setEnabledRegions(Collections.emptyList());
        dto.setRefreshIntervalMinutes(42);
        dto.setCreatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}