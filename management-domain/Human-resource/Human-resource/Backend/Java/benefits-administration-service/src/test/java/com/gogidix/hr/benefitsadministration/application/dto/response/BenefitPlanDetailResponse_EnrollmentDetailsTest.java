package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitPlanDetailResponse;
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
class BenefitPlanDetailResponse_EnrollmentDetailsTest {

        @Test
    void testBuilder() {
        BenefitPlanDetailResponse.EnrollmentDetails dto = BenefitPlanDetailResponse.EnrollmentDetails.builder()
                        .isOpenForEnrollment(true)
            .enrollmentStartDate(LocalDate.of(2025,1,15))
            .enrollmentEndDate(LocalDate.of(2025,1,15))
            .enrollmentWindowDays(42)
            .hasWaitingPeriod(true)
            .waitingPeriodDays(42)
            .requiresEvidence(true)
            .requiredDocuments(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIsOpenForEnrollment());
        assertEquals(LocalDate.of(2025,1,15), dto.getEnrollmentStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEnrollmentEndDate());
        assertEquals(42, dto.getEnrollmentWindowDays());
        assertTrue(dto.getHasWaitingPeriod());
        assertEquals(42, dto.getWaitingPeriodDays());
        assertTrue(dto.getRequiresEvidence());
    }

    @Test
    void testSettersAndGetters() {
        BenefitPlanDetailResponse.EnrollmentDetails dto = new BenefitPlanDetailResponse.EnrollmentDetails();
        dto.setIsOpenForEnrollment(true);
        dto.setEnrollmentStartDate(LocalDate.of(2025,6,1));
        dto.setEnrollmentEndDate(LocalDate.of(2025,6,1));
        dto.setEnrollmentWindowDays(99);
        dto.setHasWaitingPeriod(true);
        dto.setWaitingPeriodDays(99);
        dto.setRequiresEvidence(true);
        assertTrue(dto.getIsOpenForEnrollment());
        assertEquals(LocalDate.of(2025,6,1), dto.getEnrollmentStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEnrollmentEndDate());
        assertEquals(99, dto.getEnrollmentWindowDays());
        assertTrue(dto.getHasWaitingPeriod());
        assertEquals(99, dto.getWaitingPeriodDays());
        assertTrue(dto.getRequiresEvidence());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitPlanDetailResponse.EnrollmentDetails dto1 = BenefitPlanDetailResponse.EnrollmentDetails.builder()
                        .isOpenForEnrollment(true)
            .enrollmentStartDate(LocalDate.of(2025,1,15))
            .enrollmentEndDate(LocalDate.of(2025,1,15))
            .enrollmentWindowDays(42)
            .hasWaitingPeriod(true)
            .waitingPeriodDays(42)
            .requiresEvidence(true)
            .requiredDocuments(Collections.emptyList())
            .build();
        BenefitPlanDetailResponse.EnrollmentDetails dto2 = BenefitPlanDetailResponse.EnrollmentDetails.builder()
                        .isOpenForEnrollment(true)
            .enrollmentStartDate(LocalDate.of(2025,1,15))
            .enrollmentEndDate(LocalDate.of(2025,1,15))
            .enrollmentWindowDays(42)
            .hasWaitingPeriod(true)
            .waitingPeriodDays(42)
            .requiresEvidence(true)
            .requiredDocuments(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitPlanDetailResponse.EnrollmentDetails dto = BenefitPlanDetailResponse.EnrollmentDetails.builder()
                        .isOpenForEnrollment(true)
            .enrollmentStartDate(LocalDate.of(2025,1,15))
            .enrollmentEndDate(LocalDate.of(2025,1,15))
            .enrollmentWindowDays(42)
            .hasWaitingPeriod(true)
            .waitingPeriodDays(42)
            .requiresEvidence(true)
            .requiredDocuments(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}