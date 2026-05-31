package com.gogidix.sales.countrydashboard.domain.port.in;

import com.gogidix.sales.countrydashboard.domain.port.in.CountryDashboardCommand;
import com.gogidix.sales.countrydashboard.domain.valueobject.MetricType;
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
class CountryDashboardCommand_AddKPICommandTest {

        @Test
    void testSettersAndGetters() {
        CountryDashboardCommand.AddKPICommand dto = new CountryDashboardCommand.AddKPICommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setName("val-name");
        dto.setTarget("val-target");
        dto.setWeight(99);
        dto.setIsCritical(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-target", dto.getTarget());
        assertEquals(99, dto.getWeight());
        assertTrue(dto.getIsCritical());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardCommand.AddKPICommand dto1 = new CountryDashboardCommand.AddKPICommand();
        CountryDashboardCommand.AddKPICommand dto2 = new CountryDashboardCommand.AddKPICommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setName("test");
        dto1.setType(MetricType.TOTAL_REVENUE);
        dto1.setValue(null);
        dto1.setTarget("test");
        dto1.setWeight(42);
        dto1.setIsCritical(true);
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setName("test");
        dto2.setType(MetricType.TOTAL_REVENUE);
        dto2.setValue(null);
        dto2.setTarget("test");
        dto2.setWeight(42);
        dto2.setIsCritical(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CountryDashboardCommand.AddKPICommand dto = new CountryDashboardCommand.AddKPICommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setName("test");
        dto.setType(MetricType.TOTAL_REVENUE);
        dto.setValue(null);
        dto.setTarget("test");
        dto.setWeight(42);
        dto.setIsCritical(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CountryDashboardCommand.AddKPICommand dto = new CountryDashboardCommand.AddKPICommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setName("test");
        dto.setType(MetricType.TOTAL_REVENUE);
        dto.setValue(null);
        dto.setTarget("test");
        dto.setWeight(42);
        dto.setIsCritical(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}