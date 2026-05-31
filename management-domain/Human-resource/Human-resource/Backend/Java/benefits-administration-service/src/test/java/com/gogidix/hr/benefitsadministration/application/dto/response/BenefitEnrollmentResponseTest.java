package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitEnrollmentResponse;
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
class BenefitEnrollmentResponseTest {

        @Test
    void testBuilder() {
        BenefitEnrollmentResponse dto = BenefitEnrollmentResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .employeeId("test-employeeId")
            .planId("test-planId")
            .planName("test-planName")
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .dependents(Collections.emptyList())
            .employeePremium(BigDecimal.TEN)
            .employerPremium(BigDecimal.TEN)
            .totalPremium(BigDecimal.TEN)
            .currency("test-currency")
            .notes("test-notes")
            .waiveCoverage(true)
            .waiverReason("test-waiverReason")
            .evidenceDocuments(Collections.emptyList())
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-planId", dto.getPlanId());
        assertEquals("test-planName", dto.getPlanName());
        assertEquals("test-coverageLevel", dto.getCoverageLevel());
        assertEquals("test-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpiryDate());
        assertEquals(BigDecimal.TEN, dto.getEmployeePremium());
        assertEquals(BigDecimal.TEN, dto.getEmployerPremium());
        assertEquals(BigDecimal.TEN, dto.getTotalPremium());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getWaiveCoverage());
        assertEquals("test-waiverReason", dto.getWaiverReason());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        BenefitEnrollmentResponse dto = new BenefitEnrollmentResponse();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setEmployeeId("val-employeeId");
        dto.setPlanId("val-planId");
        dto.setPlanName("val-planName");
        dto.setCoverageLevel("val-coverageLevel");
        dto.setCoverageOptionCode("val-coverageOptionCode");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setExpiryDate(LocalDate.of(2025,6,1));
        dto.setEmployeePremium(BigDecimal.ONE);
        dto.setEmployerPremium(BigDecimal.ONE);
        dto.setTotalPremium(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setNotes("val-notes");
        dto.setWaiveCoverage(true);
        dto.setWaiverReason("val-waiverReason");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-planId", dto.getPlanId());
        assertEquals("val-planName", dto.getPlanName());
        assertEquals("val-coverageLevel", dto.getCoverageLevel());
        assertEquals("val-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpiryDate());
        assertEquals(BigDecimal.ONE, dto.getEmployeePremium());
        assertEquals(BigDecimal.ONE, dto.getEmployerPremium());
        assertEquals(BigDecimal.ONE, dto.getTotalPremium());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getWaiveCoverage());
        assertEquals("val-waiverReason", dto.getWaiverReason());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitEnrollmentResponse dto1 = BenefitEnrollmentResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .employeeId("test-employeeId")
            .planId("test-planId")
            .planName("test-planName")
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .dependents(Collections.emptyList())
            .employeePremium(BigDecimal.TEN)
            .employerPremium(BigDecimal.TEN)
            .totalPremium(BigDecimal.TEN)
            .currency("test-currency")
            .notes("test-notes")
            .waiveCoverage(true)
            .waiverReason("test-waiverReason")
            .evidenceDocuments(Collections.emptyList())
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedBy("test-updatedBy")
            .build();
        BenefitEnrollmentResponse dto2 = BenefitEnrollmentResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .employeeId("test-employeeId")
            .planId("test-planId")
            .planName("test-planName")
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .dependents(Collections.emptyList())
            .employeePremium(BigDecimal.TEN)
            .employerPremium(BigDecimal.TEN)
            .totalPremium(BigDecimal.TEN)
            .currency("test-currency")
            .notes("test-notes")
            .waiveCoverage(true)
            .waiverReason("test-waiverReason")
            .evidenceDocuments(Collections.emptyList())
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitEnrollmentResponse dto = BenefitEnrollmentResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .employeeId("test-employeeId")
            .planId("test-planId")
            .planName("test-planName")
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .dependents(Collections.emptyList())
            .employeePremium(BigDecimal.TEN)
            .employerPremium(BigDecimal.TEN)
            .totalPremium(BigDecimal.TEN)
            .currency("test-currency")
            .notes("test-notes")
            .waiveCoverage(true)
            .waiverReason("test-waiverReason")
            .evidenceDocuments(Collections.emptyList())
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}