package com.gogidix.hr.globalcompliance.application.dto.response;

import com.gogidix.hr.globalcompliance.application.dto.response.CountryDashboardDto;
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
class CountryDashboardDtoTest {

        @Test
    void testBuilder() {
        CountryDashboardDto dto = CountryDashboardDto.builder()
                        .countryCode("test-countryCode")
            .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .pendingChecks(42L)
            .overdueChecks(42L)
            .totalIssues(42L)
            .openIssues(42L)
            .criticalIssues(42L)
            .averageComplianceScore(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals(42L, dto.getTotalRequirements());
        assertEquals(42L, dto.getActiveRequirements());
        assertEquals(42L, dto.getTotalChecks());
        assertEquals(42L, dto.getPendingChecks());
        assertEquals(42L, dto.getOverdueChecks());
        assertEquals(42L, dto.getTotalIssues());
        assertEquals(42L, dto.getOpenIssues());
        assertEquals(42L, dto.getCriticalIssues());
    }

    @Test
    void testSettersAndGetters() {
        CountryDashboardDto dto = new CountryDashboardDto();
        dto.setCountryCode("val-countryCode");
        assertEquals("val-countryCode", dto.getCountryCode());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardDto dto1 = CountryDashboardDto.builder()
                        .countryCode("test-countryCode")
            .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .pendingChecks(42L)
            .overdueChecks(42L)
            .totalIssues(42L)
            .openIssues(42L)
            .criticalIssues(42L)
            .averageComplianceScore(null)
            .build();
        CountryDashboardDto dto2 = CountryDashboardDto.builder()
                        .countryCode("test-countryCode")
            .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .pendingChecks(42L)
            .overdueChecks(42L)
            .totalIssues(42L)
            .openIssues(42L)
            .criticalIssues(42L)
            .averageComplianceScore(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryDashboardDto dto = CountryDashboardDto.builder()
                        .countryCode("test-countryCode")
            .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .pendingChecks(42L)
            .overdueChecks(42L)
            .totalIssues(42L)
            .openIssues(42L)
            .criticalIssues(42L)
            .averageComplianceScore(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}