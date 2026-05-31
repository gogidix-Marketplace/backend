package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.EligibilityCheckResponse;
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
class EligibilityCheckResponse_EligibilityDetailsTest {

        @Test
    void testBuilder() {
        EligibilityCheckResponse.EligibilityDetails dto = EligibilityCheckResponse.EligibilityDetails.builder()
                        .meetsAgeRequirement(true)
            .meetsTenureRequirement(true)
            .meetsEmploymentTypeRequirement(true)
            .meetsHoursRequirement(true)
            .tenureDays(42)
            .employmentType("test-employmentType")
            .hoursPerWeek(null)
            .isFullTime(true)
            .hireDate(LocalDate.of(2025,1,15))
            .jobGrade("test-jobGrade")
            .department("test-department")
            .build();
        assertNotNull(dto);
        assertTrue(dto.getMeetsAgeRequirement());
        assertTrue(dto.getMeetsTenureRequirement());
        assertTrue(dto.getMeetsEmploymentTypeRequirement());
        assertTrue(dto.getMeetsHoursRequirement());
        assertEquals(42, dto.getTenureDays());
        assertEquals("test-employmentType", dto.getEmploymentType());
        assertTrue(dto.getIsFullTime());
        assertEquals(LocalDate.of(2025,1,15), dto.getHireDate());
        assertEquals("test-jobGrade", dto.getJobGrade());
        assertEquals("test-department", dto.getDepartment());
    }

    @Test
    void testSettersAndGetters() {
        EligibilityCheckResponse.EligibilityDetails dto = new EligibilityCheckResponse.EligibilityDetails();
        dto.setMeetsAgeRequirement(true);
        dto.setMeetsTenureRequirement(true);
        dto.setMeetsEmploymentTypeRequirement(true);
        dto.setMeetsHoursRequirement(true);
        dto.setTenureDays(99);
        dto.setEmploymentType("val-employmentType");
        dto.setIsFullTime(true);
        dto.setHireDate(LocalDate.of(2025,6,1));
        dto.setJobGrade("val-jobGrade");
        dto.setDepartment("val-department");
        assertTrue(dto.getMeetsAgeRequirement());
        assertTrue(dto.getMeetsTenureRequirement());
        assertTrue(dto.getMeetsEmploymentTypeRequirement());
        assertTrue(dto.getMeetsHoursRequirement());
        assertEquals(99, dto.getTenureDays());
        assertEquals("val-employmentType", dto.getEmploymentType());
        assertTrue(dto.getIsFullTime());
        assertEquals(LocalDate.of(2025,6,1), dto.getHireDate());
        assertEquals("val-jobGrade", dto.getJobGrade());
        assertEquals("val-department", dto.getDepartment());
    }

    @Test
    void testEqualsAndHashCode() {
        EligibilityCheckResponse.EligibilityDetails dto1 = EligibilityCheckResponse.EligibilityDetails.builder()
                        .meetsAgeRequirement(true)
            .meetsTenureRequirement(true)
            .meetsEmploymentTypeRequirement(true)
            .meetsHoursRequirement(true)
            .tenureDays(42)
            .employmentType("test-employmentType")
            .hoursPerWeek(null)
            .isFullTime(true)
            .hireDate(LocalDate.of(2025,1,15))
            .jobGrade("test-jobGrade")
            .department("test-department")
            .build();
        EligibilityCheckResponse.EligibilityDetails dto2 = EligibilityCheckResponse.EligibilityDetails.builder()
                        .meetsAgeRequirement(true)
            .meetsTenureRequirement(true)
            .meetsEmploymentTypeRequirement(true)
            .meetsHoursRequirement(true)
            .tenureDays(42)
            .employmentType("test-employmentType")
            .hoursPerWeek(null)
            .isFullTime(true)
            .hireDate(LocalDate.of(2025,1,15))
            .jobGrade("test-jobGrade")
            .department("test-department")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        EligibilityCheckResponse.EligibilityDetails dto = EligibilityCheckResponse.EligibilityDetails.builder()
                        .meetsAgeRequirement(true)
            .meetsTenureRequirement(true)
            .meetsEmploymentTypeRequirement(true)
            .meetsHoursRequirement(true)
            .tenureDays(42)
            .employmentType("test-employmentType")
            .hoursPerWeek(null)
            .isFullTime(true)
            .hireDate(LocalDate.of(2025,1,15))
            .jobGrade("test-jobGrade")
            .department("test-department")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}