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
class BenefitPlanDetailResponse_PlanStatisticsTest {

        @Test
    void testBuilder() {
        BenefitPlanDetailResponse.PlanStatistics dto = BenefitPlanDetailResponse.PlanStatistics.builder()
                        .totalEnrollments(42L)
            .activeEnrollments(42L)
            .pendingEnrollments(42L)
            .averagePremium(null)
            .minEnrollmentCount(42)
            .maxEnrollmentCount(42)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalEnrollments());
        assertEquals(42L, dto.getActiveEnrollments());
        assertEquals(42L, dto.getPendingEnrollments());
        assertEquals(42, dto.getMinEnrollmentCount());
        assertEquals(42, dto.getMaxEnrollmentCount());
    }

    @Test
    void testSettersAndGetters() {
        BenefitPlanDetailResponse.PlanStatistics dto = new BenefitPlanDetailResponse.PlanStatistics();
        dto.setMinEnrollmentCount(99);
        dto.setMaxEnrollmentCount(99);
        assertEquals(99, dto.getMinEnrollmentCount());
        assertEquals(99, dto.getMaxEnrollmentCount());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitPlanDetailResponse.PlanStatistics dto1 = BenefitPlanDetailResponse.PlanStatistics.builder()
                        .totalEnrollments(42L)
            .activeEnrollments(42L)
            .pendingEnrollments(42L)
            .averagePremium(null)
            .minEnrollmentCount(42)
            .maxEnrollmentCount(42)
            .build();
        BenefitPlanDetailResponse.PlanStatistics dto2 = BenefitPlanDetailResponse.PlanStatistics.builder()
                        .totalEnrollments(42L)
            .activeEnrollments(42L)
            .pendingEnrollments(42L)
            .averagePremium(null)
            .minEnrollmentCount(42)
            .maxEnrollmentCount(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitPlanDetailResponse.PlanStatistics dto = BenefitPlanDetailResponse.PlanStatistics.builder()
                        .totalEnrollments(42L)
            .activeEnrollments(42L)
            .pendingEnrollments(42L)
            .averagePremium(null)
            .minEnrollmentCount(42)
            .maxEnrollmentCount(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}