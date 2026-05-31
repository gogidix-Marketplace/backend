package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.DiversityResponseDto;
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
class DiversityResponseDtoTest {

        @Test
    void testBuilder() {
        DiversityResponseDto dto = DiversityResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .period("test-period")
            .totalEmployees(42)
            .genderDistribution(Collections.emptyMap())
            .ageDistribution(Collections.emptyMap())
            .nationalityDistribution(Collections.emptyMap())
            .ethnicityDistribution(Collections.emptyMap())
            .educationLevelDistribution(Collections.emptyMap())
            .genderDiversityScore(null)
            .nationalDiversityScore(null)
            .overallDiversityScore(null)
            .womenInLeadershipPercentage(null)
            .womenInLeadershipCount(42)
            .totalLeadershipCount(42)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-countryName", dto.getCountryName());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-period", dto.getPeriod());
        assertEquals(42, dto.getTotalEmployees());
        assertEquals(42, dto.getWomenInLeadershipCount());
        assertEquals(42, dto.getTotalLeadershipCount());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        DiversityResponseDto dto = new DiversityResponseDto();
        dto.setId("val-id");
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setRegionCode("val-regionCode");
        dto.setPeriod("val-period");
        dto.setTotalEmployees(99);
        dto.setWomenInLeadershipCount(99);
        dto.setTotalLeadershipCount(99);
        dto.setIsActive(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-period", dto.getPeriod());
        assertEquals(99, dto.getTotalEmployees());
        assertEquals(99, dto.getWomenInLeadershipCount());
        assertEquals(99, dto.getTotalLeadershipCount());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        DiversityResponseDto dto1 = DiversityResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .period("test-period")
            .totalEmployees(42)
            .genderDistribution(Collections.emptyMap())
            .ageDistribution(Collections.emptyMap())
            .nationalityDistribution(Collections.emptyMap())
            .ethnicityDistribution(Collections.emptyMap())
            .educationLevelDistribution(Collections.emptyMap())
            .genderDiversityScore(null)
            .nationalDiversityScore(null)
            .overallDiversityScore(null)
            .womenInLeadershipPercentage(null)
            .womenInLeadershipCount(42)
            .totalLeadershipCount(42)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        DiversityResponseDto dto2 = DiversityResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .period("test-period")
            .totalEmployees(42)
            .genderDistribution(Collections.emptyMap())
            .ageDistribution(Collections.emptyMap())
            .nationalityDistribution(Collections.emptyMap())
            .ethnicityDistribution(Collections.emptyMap())
            .educationLevelDistribution(Collections.emptyMap())
            .genderDiversityScore(null)
            .nationalDiversityScore(null)
            .overallDiversityScore(null)
            .womenInLeadershipPercentage(null)
            .womenInLeadershipCount(42)
            .totalLeadershipCount(42)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DiversityResponseDto dto = DiversityResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .period("test-period")
            .totalEmployees(42)
            .genderDistribution(Collections.emptyMap())
            .ageDistribution(Collections.emptyMap())
            .nationalityDistribution(Collections.emptyMap())
            .ethnicityDistribution(Collections.emptyMap())
            .educationLevelDistribution(Collections.emptyMap())
            .genderDiversityScore(null)
            .nationalDiversityScore(null)
            .overallDiversityScore(null)
            .womenInLeadershipPercentage(null)
            .womenInLeadershipCount(42)
            .totalLeadershipCount(42)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}