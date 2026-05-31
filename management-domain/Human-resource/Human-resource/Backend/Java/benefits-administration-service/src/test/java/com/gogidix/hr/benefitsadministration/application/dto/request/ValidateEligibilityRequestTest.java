package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.ValidateEligibilityRequest;
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
class ValidateEligibilityRequestTest {

        @Test
    void testBuilder() {
        ValidateEligibilityRequest dto = ValidateEligibilityRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .tenantId("test-tenantId")
            .hireDate(LocalDate.of(2025,1,15))
            .terminationDate(LocalDate.of(2025,1,15))
            .employmentStatus("test-employmentStatus")
            .employmentType("test-employmentType")
            .department("test-department")
            .jobGrade("test-jobGrade")
            .hoursPerWeek(null)
            .isFullTime(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .age(42)
            .gender("test-gender")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-planId", dto.getPlanId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,1,15), dto.getHireDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getTerminationDate());
        assertEquals("test-employmentStatus", dto.getEmploymentStatus());
        assertEquals("test-employmentType", dto.getEmploymentType());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-jobGrade", dto.getJobGrade());
        assertTrue(dto.getIsFullTime());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(42, dto.getAge());
        assertEquals("test-gender", dto.getGender());
        assertEquals(LocalDate.of(2025,1,15), dto.getDateOfBirth());
    }

    @Test
    void testSettersAndGetters() {
        ValidateEligibilityRequest dto = new ValidateEligibilityRequest();
        dto.setEmployeeId("val-employeeId");
        dto.setPlanId("val-planId");
        dto.setTenantId("val-tenantId");
        dto.setHireDate(LocalDate.of(2025,6,1));
        dto.setTerminationDate(LocalDate.of(2025,6,1));
        dto.setEmploymentStatus("val-employmentStatus");
        dto.setEmploymentType("val-employmentType");
        dto.setDepartment("val-department");
        dto.setJobGrade("val-jobGrade");
        dto.setIsFullTime(true);
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setAge(99);
        dto.setGender("val-gender");
        dto.setDateOfBirth(LocalDate.of(2025,6,1));
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-planId", dto.getPlanId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getHireDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getTerminationDate());
        assertEquals("val-employmentStatus", dto.getEmploymentStatus());
        assertEquals("val-employmentType", dto.getEmploymentType());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-jobGrade", dto.getJobGrade());
        assertTrue(dto.getIsFullTime());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(99, dto.getAge());
        assertEquals("val-gender", dto.getGender());
        assertEquals(LocalDate.of(2025,6,1), dto.getDateOfBirth());
    }

    @Test
    void testEqualsAndHashCode() {
        ValidateEligibilityRequest dto1 = ValidateEligibilityRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .tenantId("test-tenantId")
            .hireDate(LocalDate.of(2025,1,15))
            .terminationDate(LocalDate.of(2025,1,15))
            .employmentStatus("test-employmentStatus")
            .employmentType("test-employmentType")
            .department("test-department")
            .jobGrade("test-jobGrade")
            .hoursPerWeek(null)
            .isFullTime(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .age(42)
            .gender("test-gender")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .build();
        ValidateEligibilityRequest dto2 = ValidateEligibilityRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .tenantId("test-tenantId")
            .hireDate(LocalDate.of(2025,1,15))
            .terminationDate(LocalDate.of(2025,1,15))
            .employmentStatus("test-employmentStatus")
            .employmentType("test-employmentType")
            .department("test-department")
            .jobGrade("test-jobGrade")
            .hoursPerWeek(null)
            .isFullTime(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .age(42)
            .gender("test-gender")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ValidateEligibilityRequest dto = ValidateEligibilityRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .tenantId("test-tenantId")
            .hireDate(LocalDate.of(2025,1,15))
            .terminationDate(LocalDate.of(2025,1,15))
            .employmentStatus("test-employmentStatus")
            .employmentType("test-employmentType")
            .department("test-department")
            .jobGrade("test-jobGrade")
            .hoursPerWeek(null)
            .isFullTime(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .age(42)
            .gender("test-gender")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}