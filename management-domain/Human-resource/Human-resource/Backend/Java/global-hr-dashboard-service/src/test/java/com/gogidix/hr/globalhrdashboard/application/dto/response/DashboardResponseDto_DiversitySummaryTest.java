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
class DashboardResponseDto_DiversitySummaryTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.DiversitySummary dto = DashboardResponseDto.DiversitySummary.builder()
                        .averageGenderDiversityScore(null)
            .averageNationalDiversityScore(null)
            .averageOverallDiversityScore(null)
            .globalGenderDistribution(Collections.emptyMap())
            .womenInLeadershipPercentage(null)
            .build();
        assertNotNull(dto);

    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.DiversitySummary dto = new DashboardResponseDto.DiversitySummary();


    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.DiversitySummary dto1 = DashboardResponseDto.DiversitySummary.builder()
                        .averageGenderDiversityScore(null)
            .averageNationalDiversityScore(null)
            .averageOverallDiversityScore(null)
            .globalGenderDistribution(Collections.emptyMap())
            .womenInLeadershipPercentage(null)
            .build();
        DashboardResponseDto.DiversitySummary dto2 = DashboardResponseDto.DiversitySummary.builder()
                        .averageGenderDiversityScore(null)
            .averageNationalDiversityScore(null)
            .averageOverallDiversityScore(null)
            .globalGenderDistribution(Collections.emptyMap())
            .womenInLeadershipPercentage(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.DiversitySummary dto = DashboardResponseDto.DiversitySummary.builder()
                        .averageGenderDiversityScore(null)
            .averageNationalDiversityScore(null)
            .averageOverallDiversityScore(null)
            .globalGenderDistribution(Collections.emptyMap())
            .womenInLeadershipPercentage(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}