package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.CreateBenefitEnrollmentRequest;
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
class CreateBenefitEnrollmentRequestTest {

        @Test
    void testBuilder() {
        CreateBenefitEnrollmentRequest dto = CreateBenefitEnrollmentRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .tenantId("test-tenantId")
            .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .dependents(Collections.emptyList())
            .coverageOptionCode("test-coverageOptionCode")
            .notes("test-notes")
            .waiveCoverage(true)
            .waiverReason("test-waiverReason")
            .evidenceDocuments(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-planId", dto.getPlanId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-coverageLevel", dto.getCoverageLevel());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpiryDate());
        assertEquals("test-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getWaiveCoverage());
        assertEquals("test-waiverReason", dto.getWaiverReason());
    }

    @Test
    void testSettersAndGetters() {
        CreateBenefitEnrollmentRequest dto = new CreateBenefitEnrollmentRequest();
        dto.setEmployeeId("val-employeeId");
        dto.setPlanId("val-planId");
        dto.setTenantId("val-tenantId");
        dto.setCoverageLevel("val-coverageLevel");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setExpiryDate(LocalDate.of(2025,6,1));
        dto.setCoverageOptionCode("val-coverageOptionCode");
        dto.setNotes("val-notes");
        dto.setWaiveCoverage(true);
        dto.setWaiverReason("val-waiverReason");
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-planId", dto.getPlanId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-coverageLevel", dto.getCoverageLevel());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpiryDate());
        assertEquals("val-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getWaiveCoverage());
        assertEquals("val-waiverReason", dto.getWaiverReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateBenefitEnrollmentRequest dto1 = CreateBenefitEnrollmentRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .tenantId("test-tenantId")
            .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .dependents(Collections.emptyList())
            .coverageOptionCode("test-coverageOptionCode")
            .notes("test-notes")
            .waiveCoverage(true)
            .waiverReason("test-waiverReason")
            .evidenceDocuments(Collections.emptyList())
            .build();
        CreateBenefitEnrollmentRequest dto2 = CreateBenefitEnrollmentRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .tenantId("test-tenantId")
            .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .dependents(Collections.emptyList())
            .coverageOptionCode("test-coverageOptionCode")
            .notes("test-notes")
            .waiveCoverage(true)
            .waiverReason("test-waiverReason")
            .evidenceDocuments(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreateBenefitEnrollmentRequest dto = CreateBenefitEnrollmentRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .tenantId("test-tenantId")
            .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .dependents(Collections.emptyList())
            .coverageOptionCode("test-coverageOptionCode")
            .notes("test-notes")
            .waiveCoverage(true)
            .waiverReason("test-waiverReason")
            .evidenceDocuments(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}