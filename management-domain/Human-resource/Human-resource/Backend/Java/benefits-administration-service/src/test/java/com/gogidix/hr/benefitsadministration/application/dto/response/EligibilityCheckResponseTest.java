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
class EligibilityCheckResponseTest {

        @Test
    void testBuilder() {
        EligibilityCheckResponse dto = EligibilityCheckResponse.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .isEligible(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .eligibilityDetails(null)
            .requirements(Collections.emptyList())
            .missingRequirements(Collections.emptyList())
            .ineligibilityReason("test-ineligibilityReason")
            .eligibilityDate(LocalDate.of(2025,1,15))
            .isWithinEnrollmentWindow(true)
            .enrollmentWindowStart(LocalDate.of(2025,1,15))
            .enrollmentWindowEnd(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-planId", dto.getPlanId());
        assertTrue(dto.getIsEligible());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals("test-ineligibilityReason", dto.getIneligibilityReason());
        assertEquals(LocalDate.of(2025,1,15), dto.getEligibilityDate());
        assertTrue(dto.getIsWithinEnrollmentWindow());
        assertEquals(LocalDate.of(2025,1,15), dto.getEnrollmentWindowStart());
        assertEquals(LocalDate.of(2025,1,15), dto.getEnrollmentWindowEnd());
    }

    @Test
    void testSettersAndGetters() {
        EligibilityCheckResponse dto = new EligibilityCheckResponse();
        dto.setEmployeeId("val-employeeId");
        dto.setPlanId("val-planId");
        dto.setIsEligible(true);
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setIneligibilityReason("val-ineligibilityReason");
        dto.setEligibilityDate(LocalDate.of(2025,6,1));
        dto.setIsWithinEnrollmentWindow(true);
        dto.setEnrollmentWindowStart(LocalDate.of(2025,6,1));
        dto.setEnrollmentWindowEnd(LocalDate.of(2025,6,1));
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-planId", dto.getPlanId());
        assertTrue(dto.getIsEligible());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals("val-ineligibilityReason", dto.getIneligibilityReason());
        assertEquals(LocalDate.of(2025,6,1), dto.getEligibilityDate());
        assertTrue(dto.getIsWithinEnrollmentWindow());
        assertEquals(LocalDate.of(2025,6,1), dto.getEnrollmentWindowStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getEnrollmentWindowEnd());
    }

    @Test
    void testEqualsAndHashCode() {
        EligibilityCheckResponse dto1 = EligibilityCheckResponse.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .isEligible(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .eligibilityDetails(null)
            .requirements(Collections.emptyList())
            .missingRequirements(Collections.emptyList())
            .ineligibilityReason("test-ineligibilityReason")
            .eligibilityDate(LocalDate.of(2025,1,15))
            .isWithinEnrollmentWindow(true)
            .enrollmentWindowStart(LocalDate.of(2025,1,15))
            .enrollmentWindowEnd(LocalDate.of(2025,1,15))
            .build();
        EligibilityCheckResponse dto2 = EligibilityCheckResponse.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .isEligible(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .eligibilityDetails(null)
            .requirements(Collections.emptyList())
            .missingRequirements(Collections.emptyList())
            .ineligibilityReason("test-ineligibilityReason")
            .eligibilityDate(LocalDate.of(2025,1,15))
            .isWithinEnrollmentWindow(true)
            .enrollmentWindowStart(LocalDate.of(2025,1,15))
            .enrollmentWindowEnd(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        EligibilityCheckResponse dto = EligibilityCheckResponse.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .isEligible(true)
            .effectiveDate(LocalDate.of(2025,1,15))
            .eligibilityDetails(null)
            .requirements(Collections.emptyList())
            .missingRequirements(Collections.emptyList())
            .ineligibilityReason("test-ineligibilityReason")
            .eligibilityDate(LocalDate.of(2025,1,15))
            .isWithinEnrollmentWindow(true)
            .enrollmentWindowStart(LocalDate.of(2025,1,15))
            .enrollmentWindowEnd(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}