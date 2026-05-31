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
class DashboardCommand_UpdateExecutiveSummaryCommandTest {

        @Test
    void testSettersAndGetters() {
        DashboardCommand.UpdateExecutiveSummaryCommand dto = new DashboardCommand.UpdateExecutiveSummaryCommand();
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setHeadline("val-headline");
        dto.setKeyHighlight("val-keyHighlight");
        dto.setOverallSentiment("val-overallSentiment");
        dto.setRiskScore(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-headline", dto.getHeadline());
        assertEquals("val-keyHighlight", dto.getKeyHighlight());
        assertEquals("val-overallSentiment", dto.getOverallSentiment());
        assertEquals(BigDecimal.ONE, dto.getRiskScore());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardCommand.UpdateExecutiveSummaryCommand dto1 = new DashboardCommand.UpdateExecutiveSummaryCommand();
        DashboardCommand.UpdateExecutiveSummaryCommand dto2 = new DashboardCommand.UpdateExecutiveSummaryCommand();
        dto1.setTenantId("test");
        dto1.setDashboardId("test");
        dto1.setHeadline("test");
        dto1.setKeyHighlight("test");
        dto1.setTopPerformers(Collections.emptyList());
        dto1.setAreasForImprovement(Collections.emptyList());
        dto1.setOverallSentiment("test");
        dto1.setRiskScore(BigDecimal.TEN);
        dto1.setRecommendations(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setDashboardId("test");
        dto2.setHeadline("test");
        dto2.setKeyHighlight("test");
        dto2.setTopPerformers(Collections.emptyList());
        dto2.setAreasForImprovement(Collections.emptyList());
        dto2.setOverallSentiment("test");
        dto2.setRiskScore(BigDecimal.TEN);
        dto2.setRecommendations(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DashboardCommand.UpdateExecutiveSummaryCommand dto = new DashboardCommand.UpdateExecutiveSummaryCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setHeadline("test");
        dto.setKeyHighlight("test");
        dto.setTopPerformers(Collections.emptyList());
        dto.setAreasForImprovement(Collections.emptyList());
        dto.setOverallSentiment("test");
        dto.setRiskScore(BigDecimal.TEN);
        dto.setRecommendations(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DashboardCommand.UpdateExecutiveSummaryCommand dto = new DashboardCommand.UpdateExecutiveSummaryCommand();
        dto.setTenantId("test");
        dto.setDashboardId("test");
        dto.setHeadline("test");
        dto.setKeyHighlight("test");
        dto.setTopPerformers(Collections.emptyList());
        dto.setAreasForImprovement(Collections.emptyList());
        dto.setOverallSentiment("test");
        dto.setRiskScore(BigDecimal.TEN);
        dto.setRecommendations(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}