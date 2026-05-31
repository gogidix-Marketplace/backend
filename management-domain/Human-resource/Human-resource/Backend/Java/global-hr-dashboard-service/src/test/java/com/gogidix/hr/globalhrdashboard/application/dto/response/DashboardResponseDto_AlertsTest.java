package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_AlertsTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.Alerts dto = DashboardResponseDto.Alerts.builder()
                        .criticalComplianceIssues(42L)
            .highTurnoverCountries(42L)
            .lowDiversityCountries(42L)
            .atRiskCountries(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getCriticalComplianceIssues());
        assertEquals(42L, dto.getHighTurnoverCountries());
        assertEquals(42L, dto.getLowDiversityCountries());
        assertEquals(42L, dto.getAtRiskCountries());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.Alerts dto = new DashboardResponseDto.Alerts();


    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.Alerts dto1 = DashboardResponseDto.Alerts.builder()
                        .criticalComplianceIssues(42L)
            .highTurnoverCountries(42L)
            .lowDiversityCountries(42L)
            .atRiskCountries(42L)
            .build();
        DashboardResponseDto.Alerts dto2 = DashboardResponseDto.Alerts.builder()
                        .criticalComplianceIssues(42L)
            .highTurnoverCountries(42L)
            .lowDiversityCountries(42L)
            .atRiskCountries(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.Alerts dto = DashboardResponseDto.Alerts.builder()
                        .criticalComplianceIssues(42L)
            .highTurnoverCountries(42L)
            .lowDiversityCountries(42L)
            .atRiskCountries(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}