package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.DashboardResponseDto;
import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus;
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
class DashboardResponseDto_ComplianceSummaryTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.ComplianceSummary dto = DashboardResponseDto.ComplianceSummary.builder()
                        .averageScore(null)
            .globalStatus(ComplianceStatus.COMPLIANT)
            .compliantCount(42L)
            .atRiskCount(42L)
            .nonCompliantCount(42L)
            .withCriticalIssues(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getCompliantCount());
        assertEquals(42L, dto.getAtRiskCount());
        assertEquals(42L, dto.getNonCompliantCount());
        assertEquals(42L, dto.getWithCriticalIssues());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.ComplianceSummary dto = new DashboardResponseDto.ComplianceSummary();


    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.ComplianceSummary dto1 = DashboardResponseDto.ComplianceSummary.builder()
                        .averageScore(null)
            .globalStatus(ComplianceStatus.COMPLIANT)
            .compliantCount(42L)
            .atRiskCount(42L)
            .nonCompliantCount(42L)
            .withCriticalIssues(42L)
            .build();
        DashboardResponseDto.ComplianceSummary dto2 = DashboardResponseDto.ComplianceSummary.builder()
                        .averageScore(null)
            .globalStatus(ComplianceStatus.COMPLIANT)
            .compliantCount(42L)
            .atRiskCount(42L)
            .nonCompliantCount(42L)
            .withCriticalIssues(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.ComplianceSummary dto = DashboardResponseDto.ComplianceSummary.builder()
                        .averageScore(null)
            .globalStatus(ComplianceStatus.COMPLIANT)
            .compliantCount(42L)
            .atRiskCount(42L)
            .nonCompliantCount(42L)
            .withCriticalIssues(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}