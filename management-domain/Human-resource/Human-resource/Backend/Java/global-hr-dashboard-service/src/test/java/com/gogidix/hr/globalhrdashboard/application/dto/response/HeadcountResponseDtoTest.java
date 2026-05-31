package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.HeadcountResponseDto;
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
class HeadcountResponseDtoTest {

        @Test
    void testBuilder() {
        HeadcountResponseDto dto = HeadcountResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .totalHeadcount(42)
            .permanentEmployees(42)
            .contractors(42)
            .interns(42)
            .department("test-department")
            .period("test-period")
            .yoyChange(42)
            .momChange(42)
            .qoqChange(42)
            .femalePercentage(null)
            .malePercentage(null)
            .otherGenderPercentage(null)
            .avgAge(null)
            .avgTenureYears(null)
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
        assertEquals(42, dto.getTotalHeadcount());
        assertEquals(42, dto.getPermanentEmployees());
        assertEquals(42, dto.getContractors());
        assertEquals(42, dto.getInterns());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-period", dto.getPeriod());
        assertEquals(42, dto.getYoyChange());
        assertEquals(42, dto.getMomChange());
        assertEquals(42, dto.getQoqChange());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        HeadcountResponseDto dto = new HeadcountResponseDto();
        dto.setId("val-id");
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setRegionCode("val-regionCode");
        dto.setTotalHeadcount(99);
        dto.setPermanentEmployees(99);
        dto.setContractors(99);
        dto.setInterns(99);
        dto.setDepartment("val-department");
        dto.setPeriod("val-period");
        dto.setYoyChange(99);
        dto.setMomChange(99);
        dto.setQoqChange(99);
        dto.setIsActive(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals(99, dto.getTotalHeadcount());
        assertEquals(99, dto.getPermanentEmployees());
        assertEquals(99, dto.getContractors());
        assertEquals(99, dto.getInterns());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-period", dto.getPeriod());
        assertEquals(99, dto.getYoyChange());
        assertEquals(99, dto.getMomChange());
        assertEquals(99, dto.getQoqChange());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        HeadcountResponseDto dto1 = HeadcountResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .totalHeadcount(42)
            .permanentEmployees(42)
            .contractors(42)
            .interns(42)
            .department("test-department")
            .period("test-period")
            .yoyChange(42)
            .momChange(42)
            .qoqChange(42)
            .femalePercentage(null)
            .malePercentage(null)
            .otherGenderPercentage(null)
            .avgAge(null)
            .avgTenureYears(null)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        HeadcountResponseDto dto2 = HeadcountResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .totalHeadcount(42)
            .permanentEmployees(42)
            .contractors(42)
            .interns(42)
            .department("test-department")
            .period("test-period")
            .yoyChange(42)
            .momChange(42)
            .qoqChange(42)
            .femalePercentage(null)
            .malePercentage(null)
            .otherGenderPercentage(null)
            .avgAge(null)
            .avgTenureYears(null)
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
        HeadcountResponseDto dto = HeadcountResponseDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .totalHeadcount(42)
            .permanentEmployees(42)
            .contractors(42)
            .interns(42)
            .department("test-department")
            .period("test-period")
            .yoyChange(42)
            .momChange(42)
            .qoqChange(42)
            .femalePercentage(null)
            .malePercentage(null)
            .otherGenderPercentage(null)
            .avgAge(null)
            .avgTenureYears(null)
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