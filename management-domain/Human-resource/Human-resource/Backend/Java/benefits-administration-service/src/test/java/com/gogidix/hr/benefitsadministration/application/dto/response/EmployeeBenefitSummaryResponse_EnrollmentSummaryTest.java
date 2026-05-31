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
class EmployeeBenefitSummaryResponse_EnrollmentSummaryTest {

        @Test
    void testBuilder() {
        EmployeeBenefitSummaryResponse.EnrollmentSummary dto = EmployeeBenefitSummaryResponse.EnrollmentSummary.builder()
                        .enrollmentId("test-enrollmentId")
            .planName("test-planName")
            .planType("test-planType")
            .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .monthlyPremium(BigDecimal.TEN)
            .dependentCount(42)
            .isActive(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-enrollmentId", dto.getEnrollmentId());
        assertEquals("test-planName", dto.getPlanName());
        assertEquals("test-planType", dto.getPlanType());
        assertEquals("test-coverageLevel", dto.getCoverageLevel());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpiryDate());
        assertEquals("test-status", dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getMonthlyPremium());
        assertEquals(42, dto.getDependentCount());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        EmployeeBenefitSummaryResponse.EnrollmentSummary dto = new EmployeeBenefitSummaryResponse.EnrollmentSummary();
        dto.setEnrollmentId("val-enrollmentId");
        dto.setPlanName("val-planName");
        dto.setPlanType("val-planType");
        dto.setCoverageLevel("val-coverageLevel");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setExpiryDate(LocalDate.of(2025,6,1));
        dto.setStatus("val-status");
        dto.setMonthlyPremium(BigDecimal.ONE);
        dto.setDependentCount(99);
        dto.setIsActive(true);
        assertEquals("val-enrollmentId", dto.getEnrollmentId());
        assertEquals("val-planName", dto.getPlanName());
        assertEquals("val-planType", dto.getPlanType());
        assertEquals("val-coverageLevel", dto.getCoverageLevel());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpiryDate());
        assertEquals("val-status", dto.getStatus());
        assertEquals(BigDecimal.ONE, dto.getMonthlyPremium());
        assertEquals(99, dto.getDependentCount());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        EmployeeBenefitSummaryResponse.EnrollmentSummary dto1 = EmployeeBenefitSummaryResponse.EnrollmentSummary.builder()
                        .enrollmentId("test-enrollmentId")
            .planName("test-planName")
            .planType("test-planType")
            .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .monthlyPremium(BigDecimal.TEN)
            .dependentCount(42)
            .isActive(true)
            .build();
        EmployeeBenefitSummaryResponse.EnrollmentSummary dto2 = EmployeeBenefitSummaryResponse.EnrollmentSummary.builder()
                        .enrollmentId("test-enrollmentId")
            .planName("test-planName")
            .planType("test-planType")
            .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .monthlyPremium(BigDecimal.TEN)
            .dependentCount(42)
            .isActive(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        EmployeeBenefitSummaryResponse.EnrollmentSummary dto = EmployeeBenefitSummaryResponse.EnrollmentSummary.builder()
                        .enrollmentId("test-enrollmentId")
            .planName("test-planName")
            .planType("test-planType")
            .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .monthlyPremium(BigDecimal.TEN)
            .dependentCount(42)
            .isActive(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}