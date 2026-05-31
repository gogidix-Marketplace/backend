package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.RetentionResponseDto;
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
class RetentionResponseDtoTest {

        @Test
    void testBuilder() {
        RetentionResponseDto dto = RetentionResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .period("test-period")
            .retentionRate(null)
            .turnoverRate(null)
            .totalEmployees(42)
            .voluntaryDepartures(42)
            .involuntaryDepartures(42)
            .avgTenure(null)
            .medianTenure(null)
            .departureReasons(Collections.emptyList())
            .newHireRetentionRate(null)
            .totalNewHires(42)
            .retainedNewHires(42)
            .topPerformerRetentionRate(null)
            .totalTopPerformers(42)
            .retainedTopPerformers(42)
            .promotionRate(null)
            .totalPromotions(42)
            .internalMobilityRate(null)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
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
        assertEquals(42, dto.getVoluntaryDepartures());
        assertEquals(42, dto.getInvoluntaryDepartures());
        assertEquals(42, dto.getTotalNewHires());
        assertEquals(42, dto.getRetainedNewHires());
        assertEquals(42, dto.getTotalTopPerformers());
        assertEquals(42, dto.getRetainedTopPerformers());
        assertEquals(42, dto.getTotalPromotions());
        assertTrue(dto.getIsActive());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        RetentionResponseDto dto = new RetentionResponseDto();
        dto.setId("val-id");
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setRegionCode("val-regionCode");
        dto.setPeriod("val-period");
        dto.setTotalEmployees(99);
        dto.setVoluntaryDepartures(99);
        dto.setInvoluntaryDepartures(99);
        dto.setTotalNewHires(99);
        dto.setRetainedNewHires(99);
        dto.setTotalTopPerformers(99);
        dto.setRetainedTopPerformers(99);
        dto.setTotalPromotions(99);
        dto.setIsActive(true);
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-period", dto.getPeriod());
        assertEquals(99, dto.getTotalEmployees());
        assertEquals(99, dto.getVoluntaryDepartures());
        assertEquals(99, dto.getInvoluntaryDepartures());
        assertEquals(99, dto.getTotalNewHires());
        assertEquals(99, dto.getRetainedNewHires());
        assertEquals(99, dto.getTotalTopPerformers());
        assertEquals(99, dto.getRetainedTopPerformers());
        assertEquals(99, dto.getTotalPromotions());
        assertTrue(dto.getIsActive());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        RetentionResponseDto dto1 = RetentionResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .period("test-period")
            .retentionRate(null)
            .turnoverRate(null)
            .totalEmployees(42)
            .voluntaryDepartures(42)
            .involuntaryDepartures(42)
            .avgTenure(null)
            .medianTenure(null)
            .departureReasons(Collections.emptyList())
            .newHireRetentionRate(null)
            .totalNewHires(42)
            .retainedNewHires(42)
            .topPerformerRetentionRate(null)
            .totalTopPerformers(42)
            .retainedTopPerformers(42)
            .promotionRate(null)
            .totalPromotions(42)
            .internalMobilityRate(null)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        RetentionResponseDto dto2 = RetentionResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .period("test-period")
            .retentionRate(null)
            .turnoverRate(null)
            .totalEmployees(42)
            .voluntaryDepartures(42)
            .involuntaryDepartures(42)
            .avgTenure(null)
            .medianTenure(null)
            .departureReasons(Collections.emptyList())
            .newHireRetentionRate(null)
            .totalNewHires(42)
            .retainedNewHires(42)
            .topPerformerRetentionRate(null)
            .totalTopPerformers(42)
            .retainedTopPerformers(42)
            .promotionRate(null)
            .totalPromotions(42)
            .internalMobilityRate(null)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RetentionResponseDto dto = RetentionResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .period("test-period")
            .retentionRate(null)
            .turnoverRate(null)
            .totalEmployees(42)
            .voluntaryDepartures(42)
            .involuntaryDepartures(42)
            .avgTenure(null)
            .medianTenure(null)
            .departureReasons(Collections.emptyList())
            .newHireRetentionRate(null)
            .totalNewHires(42)
            .retainedNewHires(42)
            .topPerformerRetentionRate(null)
            .totalTopPerformers(42)
            .retainedTopPerformers(42)
            .promotionRate(null)
            .totalPromotions(42)
            .internalMobilityRate(null)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}