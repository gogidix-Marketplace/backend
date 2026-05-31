package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.EmployeeBenefitSummaryResponse;
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
class EmployeeBenefitSummaryResponseTest {

        @Test
    void testBuilder() {
        EmployeeBenefitSummaryResponse dto = EmployeeBenefitSummaryResponse.builder()
                        .employeeId("test-employeeId")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .department("test-department")
            .jobTitle("test-jobTitle")
            .hireDate(LocalDate.of(2025,1,15))
            .isEligibleForBenefits(true)
            .enrollments(Collections.emptyList())
            .totalMonthlyPremium(BigDecimal.TEN)
            .totalEmployerContribution(BigDecimal.TEN)
            .totalEmployeeContribution(BigDecimal.TEN)
            .totalDependents(42)
            .nextOpenEnrollmentDate(LocalDate.of(2025,1,15))
            .availableForEnrollment(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-jobTitle", dto.getJobTitle());
        assertEquals(LocalDate.of(2025,1,15), dto.getHireDate());
        assertTrue(dto.getIsEligibleForBenefits());
        assertEquals(BigDecimal.TEN, dto.getTotalMonthlyPremium());
        assertEquals(BigDecimal.TEN, dto.getTotalEmployerContribution());
        assertEquals(BigDecimal.TEN, dto.getTotalEmployeeContribution());
        assertEquals(42, dto.getTotalDependents());
        assertEquals(LocalDate.of(2025,1,15), dto.getNextOpenEnrollmentDate());
    }

    @Test
    void testSettersAndGetters() {
        EmployeeBenefitSummaryResponse dto = new EmployeeBenefitSummaryResponse();
        dto.setEmployeeId("val-employeeId");
        dto.setTenantId("val-tenantId");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setEmail("val-email");
        dto.setDepartment("val-department");
        dto.setJobTitle("val-jobTitle");
        dto.setHireDate(LocalDate.of(2025,6,1));
        dto.setIsEligibleForBenefits(true);
        dto.setTotalMonthlyPremium(BigDecimal.ONE);
        dto.setTotalEmployerContribution(BigDecimal.ONE);
        dto.setTotalEmployeeContribution(BigDecimal.ONE);
        dto.setTotalDependents(99);
        dto.setNextOpenEnrollmentDate(LocalDate.of(2025,6,1));
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-jobTitle", dto.getJobTitle());
        assertEquals(LocalDate.of(2025,6,1), dto.getHireDate());
        assertTrue(dto.getIsEligibleForBenefits());
        assertEquals(BigDecimal.ONE, dto.getTotalMonthlyPremium());
        assertEquals(BigDecimal.ONE, dto.getTotalEmployerContribution());
        assertEquals(BigDecimal.ONE, dto.getTotalEmployeeContribution());
        assertEquals(99, dto.getTotalDependents());
        assertEquals(LocalDate.of(2025,6,1), dto.getNextOpenEnrollmentDate());
    }

    @Test
    void testEqualsAndHashCode() {
        EmployeeBenefitSummaryResponse dto1 = EmployeeBenefitSummaryResponse.builder()
                        .employeeId("test-employeeId")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .department("test-department")
            .jobTitle("test-jobTitle")
            .hireDate(LocalDate.of(2025,1,15))
            .isEligibleForBenefits(true)
            .enrollments(Collections.emptyList())
            .totalMonthlyPremium(BigDecimal.TEN)
            .totalEmployerContribution(BigDecimal.TEN)
            .totalEmployeeContribution(BigDecimal.TEN)
            .totalDependents(42)
            .nextOpenEnrollmentDate(LocalDate.of(2025,1,15))
            .availableForEnrollment(Collections.emptyList())
            .build();
        EmployeeBenefitSummaryResponse dto2 = EmployeeBenefitSummaryResponse.builder()
                        .employeeId("test-employeeId")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .department("test-department")
            .jobTitle("test-jobTitle")
            .hireDate(LocalDate.of(2025,1,15))
            .isEligibleForBenefits(true)
            .enrollments(Collections.emptyList())
            .totalMonthlyPremium(BigDecimal.TEN)
            .totalEmployerContribution(BigDecimal.TEN)
            .totalEmployeeContribution(BigDecimal.TEN)
            .totalDependents(42)
            .nextOpenEnrollmentDate(LocalDate.of(2025,1,15))
            .availableForEnrollment(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        EmployeeBenefitSummaryResponse dto = EmployeeBenefitSummaryResponse.builder()
                        .employeeId("test-employeeId")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .department("test-department")
            .jobTitle("test-jobTitle")
            .hireDate(LocalDate.of(2025,1,15))
            .isEligibleForBenefits(true)
            .enrollments(Collections.emptyList())
            .totalMonthlyPremium(BigDecimal.TEN)
            .totalEmployerContribution(BigDecimal.TEN)
            .totalEmployeeContribution(BigDecimal.TEN)
            .totalDependents(42)
            .nextOpenEnrollmentDate(LocalDate.of(2025,1,15))
            .availableForEnrollment(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}