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
class ReportCommand_AddRecommendationCommandTest {

        @Test
    void testSettersAndGetters() {
        ReportCommand.AddRecommendationCommand dto = new ReportCommand.AddRecommendationCommand();
        dto.setTenantId("val-tenantId");
        dto.setReportId("val-reportId");
        dto.setRecommendation("val-recommendation");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportId", dto.getReportId());
        assertEquals("val-recommendation", dto.getRecommendation());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportCommand.AddRecommendationCommand dto1 = new ReportCommand.AddRecommendationCommand();
        ReportCommand.AddRecommendationCommand dto2 = new ReportCommand.AddRecommendationCommand();
        dto1.setTenantId("test");
        dto1.setReportId("test");
        dto1.setRecommendation("test");
        dto2.setTenantId("test");
        dto2.setReportId("test");
        dto2.setRecommendation("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportCommand.AddRecommendationCommand dto = new ReportCommand.AddRecommendationCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setRecommendation("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportCommand.AddRecommendationCommand dto = new ReportCommand.AddRecommendationCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setRecommendation("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}