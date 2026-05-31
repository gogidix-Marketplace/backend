package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.EnrollmentConfirmationResponse;
import com.gogidix.hr.benefitsadministration.domain.enums.EnrollmentStatus;
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
class EnrollmentConfirmationResponseTest {

        @Test
    void testBuilder() {
        EnrollmentConfirmationResponse dto = EnrollmentConfirmationResponse.builder()
                        .enrollmentId("test-enrollmentId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .planId("test-planId")
            .planName("test-planName")
            .coverageLevel("test-coverageLevel")
            .status(EnrollmentStatus.ACTIVE)
            .effectiveDate(LocalDate.of(2025,1,15))
            .confirmationNumber("test-confirmationNumber")
            .confirmedAt(LocalDateTime.of(2025,1,15,10,0))
            .requiresApproval(true)
            .requiresEvidence(true)
            .requiredEvidence(Collections.emptyList())
            .nextSteps("test-nextSteps")
            .message("test-message")
            .isSuccessful(true)
            .warnings(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-enrollmentId", dto.getEnrollmentId());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-employeeName", dto.getEmployeeName());
        assertEquals("test-planId", dto.getPlanId());
        assertEquals("test-planName", dto.getPlanName());
        assertEquals("test-coverageLevel", dto.getCoverageLevel());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals("test-confirmationNumber", dto.getConfirmationNumber());
        assertTrue(dto.getRequiresApproval());
        assertTrue(dto.getRequiresEvidence());
        assertEquals("test-nextSteps", dto.getNextSteps());
        assertEquals("test-message", dto.getMessage());
        assertTrue(dto.getIsSuccessful());
    }

    @Test
    void testSettersAndGetters() {
        EnrollmentConfirmationResponse dto = new EnrollmentConfirmationResponse();
        dto.setEnrollmentId("val-enrollmentId");
        dto.setEmployeeId("val-employeeId");
        dto.setEmployeeName("val-employeeName");
        dto.setPlanId("val-planId");
        dto.setPlanName("val-planName");
        dto.setCoverageLevel("val-coverageLevel");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setConfirmationNumber("val-confirmationNumber");
        dto.setRequiresApproval(true);
        dto.setRequiresEvidence(true);
        dto.setNextSteps("val-nextSteps");
        dto.setMessage("val-message");
        dto.setIsSuccessful(true);
        assertEquals("val-enrollmentId", dto.getEnrollmentId());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-employeeName", dto.getEmployeeName());
        assertEquals("val-planId", dto.getPlanId());
        assertEquals("val-planName", dto.getPlanName());
        assertEquals("val-coverageLevel", dto.getCoverageLevel());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals("val-confirmationNumber", dto.getConfirmationNumber());
        assertTrue(dto.getRequiresApproval());
        assertTrue(dto.getRequiresEvidence());
        assertEquals("val-nextSteps", dto.getNextSteps());
        assertEquals("val-message", dto.getMessage());
        assertTrue(dto.getIsSuccessful());
    }

    @Test
    void testEqualsAndHashCode() {
        EnrollmentConfirmationResponse dto1 = EnrollmentConfirmationResponse.builder()
                        .enrollmentId("test-enrollmentId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .planId("test-planId")
            .planName("test-planName")
            .coverageLevel("test-coverageLevel")
            .status(EnrollmentStatus.ACTIVE)
            .effectiveDate(LocalDate.of(2025,1,15))
            .confirmationNumber("test-confirmationNumber")
            .confirmedAt(LocalDateTime.of(2025,1,15,10,0))
            .requiresApproval(true)
            .requiresEvidence(true)
            .requiredEvidence(Collections.emptyList())
            .nextSteps("test-nextSteps")
            .message("test-message")
            .isSuccessful(true)
            .warnings(Collections.emptyList())
            .build();
        EnrollmentConfirmationResponse dto2 = EnrollmentConfirmationResponse.builder()
                        .enrollmentId("test-enrollmentId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .planId("test-planId")
            .planName("test-planName")
            .coverageLevel("test-coverageLevel")
            .status(EnrollmentStatus.ACTIVE)
            .effectiveDate(LocalDate.of(2025,1,15))
            .confirmationNumber("test-confirmationNumber")
            .confirmedAt(LocalDateTime.of(2025,1,15,10,0))
            .requiresApproval(true)
            .requiresEvidence(true)
            .requiredEvidence(Collections.emptyList())
            .nextSteps("test-nextSteps")
            .message("test-message")
            .isSuccessful(true)
            .warnings(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        EnrollmentConfirmationResponse dto = EnrollmentConfirmationResponse.builder()
                        .enrollmentId("test-enrollmentId")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .planId("test-planId")
            .planName("test-planName")
            .coverageLevel("test-coverageLevel")
            .status(EnrollmentStatus.ACTIVE)
            .effectiveDate(LocalDate.of(2025,1,15))
            .confirmationNumber("test-confirmationNumber")
            .confirmedAt(LocalDateTime.of(2025,1,15,10,0))
            .requiresApproval(true)
            .requiresEvidence(true)
            .requiredEvidence(Collections.emptyList())
            .nextSteps("test-nextSteps")
            .message("test-message")
            .isSuccessful(true)
            .warnings(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}