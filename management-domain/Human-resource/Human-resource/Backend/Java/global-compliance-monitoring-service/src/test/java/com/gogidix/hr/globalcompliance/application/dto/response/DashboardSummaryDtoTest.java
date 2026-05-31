package com.gogidix.hr.globalcompliance.application.dto.response;

import com.gogidix.hr.globalcompliance.application.dto.response.DashboardSummaryDto;
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
class DashboardSummaryDtoTest {

        @Test
    void testBuilder() {
        DashboardSummaryDto dto = DashboardSummaryDto.builder()
                        .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .pendingChecks(42L)
            .overdueChecks(42L)
            .totalIssues(42L)
            .openIssues(42L)
            .criticalIssues(42L)
            .overdueIssues(42L)
            .averageComplianceScore(null)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalRequirements());
        assertEquals(42L, dto.getActiveRequirements());
        assertEquals(42L, dto.getTotalChecks());
        assertEquals(42L, dto.getPendingChecks());
        assertEquals(42L, dto.getOverdueChecks());
        assertEquals(42L, dto.getTotalIssues());
        assertEquals(42L, dto.getOpenIssues());
        assertEquals(42L, dto.getCriticalIssues());
        assertEquals(42L, dto.getOverdueIssues());
    }

    @Test
    void testSettersAndGetters() {
        DashboardSummaryDto dto = new DashboardSummaryDto();


    }

    @Test
    void testEqualsAndHashCode() {
        DashboardSummaryDto dto1 = DashboardSummaryDto.builder()
                        .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .pendingChecks(42L)
            .overdueChecks(42L)
            .totalIssues(42L)
            .openIssues(42L)
            .criticalIssues(42L)
            .overdueIssues(42L)
            .averageComplianceScore(null)
            .build();
        DashboardSummaryDto dto2 = DashboardSummaryDto.builder()
                        .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .pendingChecks(42L)
            .overdueChecks(42L)
            .totalIssues(42L)
            .openIssues(42L)
            .criticalIssues(42L)
            .overdueIssues(42L)
            .averageComplianceScore(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardSummaryDto dto = DashboardSummaryDto.builder()
                        .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .pendingChecks(42L)
            .overdueChecks(42L)
            .totalIssues(42L)
            .openIssues(42L)
            .criticalIssues(42L)
            .overdueIssues(42L)
            .averageComplianceScore(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}