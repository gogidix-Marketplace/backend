package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.ReportCommand;
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
class ReportCommand_CreateReportCommandTest {

        @Test
    void testSettersAndGetters() {
        ReportCommand.CreateReportCommand dto = new ReportCommand.CreateReportCommand();
        dto.setTenantId("val-tenantId");
        dto.setReportType("val-reportType");
        dto.setCountryCode("val-countryCode");
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setPreparedBy("val-preparedBy");
        dto.setPreparedByName("val-preparedByName");
        dto.setDepartment("val-department");
        dto.setRegion("val-region");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportType", dto.getReportType());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals("val-preparedBy", dto.getPreparedBy());
        assertEquals("val-preparedByName", dto.getPreparedByName());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-region", dto.getRegion());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportCommand.CreateReportCommand dto1 = new ReportCommand.CreateReportCommand();
        ReportCommand.CreateReportCommand dto2 = new ReportCommand.CreateReportCommand();
        dto1.setTenantId("test");
        dto1.setReportType("test");
        dto1.setCountryCode("test");
        dto1.setPeriodStart(LocalDate.of(2025,1,1));
        dto1.setPeriodEnd(LocalDate.of(2025,1,1));
        dto1.setPreparedBy("test");
        dto1.setPreparedByName("test");
        dto1.setDepartment("test");
        dto1.setRegion("test");
        dto2.setTenantId("test");
        dto2.setReportType("test");
        dto2.setCountryCode("test");
        dto2.setPeriodStart(LocalDate.of(2025,1,1));
        dto2.setPeriodEnd(LocalDate.of(2025,1,1));
        dto2.setPreparedBy("test");
        dto2.setPreparedByName("test");
        dto2.setDepartment("test");
        dto2.setRegion("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportCommand.CreateReportCommand dto = new ReportCommand.CreateReportCommand();
        dto.setTenantId("test");
        dto.setReportType("test");
        dto.setCountryCode("test");
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setPreparedBy("test");
        dto.setPreparedByName("test");
        dto.setDepartment("test");
        dto.setRegion("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportCommand.CreateReportCommand dto = new ReportCommand.CreateReportCommand();
        dto.setTenantId("test");
        dto.setReportType("test");
        dto.setCountryCode("test");
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setPreparedBy("test");
        dto.setPreparedByName("test");
        dto.setDepartment("test");
        dto.setRegion("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}