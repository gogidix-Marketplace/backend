package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.CreateBenefitPlanRequest;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitStatus;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitType;
import com.gogidix.hr.benefitsadministration.domain.enums.DeductionFrequency;
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
class CreateBenefitPlanRequestTest {

        @Test
    void testBuilder() {
        CreateBenefitPlanRequest dto = CreateBenefitPlanRequest.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .planCode("test-planCode")
            .planName("test-planName")
            .description("test-description")
            .benefitType(BenefitType.HEALTH_INSURANCE)
            .status(BenefitStatus.ACTIVE)
            .providerId("test-providerId")
            .providerName("test-providerName")
            .employeeContribution(BigDecimal.TEN)
            .employerContribution(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency(DeductionFrequency.WEEKLY)
            .coverageOptions(Collections.emptyList())
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .enrollmentWindowDays(42)
            .requiresEvidence(true)
            .requiredDocuments(Collections.emptyList())
            .minEmployees(42)
            .maxEmployees(42)
            .isVoluntary(true)
            .isTaxable(true)
            .taxCode("test-taxCode")
            .hasWaitingPeriod(true)
            .waitingPeriodDays(42)
            .eligibilityCheckRequired(true)
            .eligibilityCriteria("test-eligibilityCriteria")
            .termsAndConditions("test-termsAndConditions")
            .summary("test-summary")
            .category("test-category")
            .priority(42)
            .contactInfo("test-contactInfo")
            .websiteUrl("test-websiteUrl")
            .brochureUrl("test-brochureUrl")
            .coveredServices(Collections.emptyList())
            .excludedServices(Collections.emptyList())
            .annualLimit(BigDecimal.TEN)
            .notes("test-notes")
            .isActive(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-planCode", dto.getPlanCode());
        assertEquals("test-planName", dto.getPlanName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-providerId", dto.getProviderId());
        assertEquals("test-providerName", dto.getProviderName());
        assertEquals(BigDecimal.TEN, dto.getEmployeeContribution());
        assertEquals(BigDecimal.TEN, dto.getEmployerContribution());
        assertEquals(BigDecimal.TEN, dto.getTotalCost());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpiryDate());
        assertEquals(42, dto.getEnrollmentWindowDays());
        assertTrue(dto.getRequiresEvidence());
        assertEquals(42, dto.getMinEmployees());
        assertEquals(42, dto.getMaxEmployees());
        assertTrue(dto.getIsVoluntary());
        assertTrue(dto.getIsTaxable());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertTrue(dto.getHasWaitingPeriod());
        assertEquals(42, dto.getWaitingPeriodDays());
        assertTrue(dto.getEligibilityCheckRequired());
        assertEquals("test-eligibilityCriteria", dto.getEligibilityCriteria());
        assertEquals("test-termsAndConditions", dto.getTermsAndConditions());
        assertEquals("test-summary", dto.getSummary());
        assertEquals("test-category", dto.getCategory());
        assertEquals(42, dto.getPriority());
        assertEquals("test-contactInfo", dto.getContactInfo());
        assertEquals("test-websiteUrl", dto.getWebsiteUrl());
        assertEquals("test-brochureUrl", dto.getBrochureUrl());
        assertEquals(BigDecimal.TEN, dto.getAnnualLimit());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        CreateBenefitPlanRequest dto = new CreateBenefitPlanRequest();
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setPlanCode("val-planCode");
        dto.setPlanName("val-planName");
        dto.setDescription("val-description");
        dto.setProviderId("val-providerId");
        dto.setProviderName("val-providerName");
        dto.setEmployeeContribution(BigDecimal.ONE);
        dto.setEmployerContribution(BigDecimal.ONE);
        dto.setTotalCost(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setExpiryDate(LocalDate.of(2025,6,1));
        dto.setEnrollmentWindowDays(99);
        dto.setRequiresEvidence(true);
        dto.setMinEmployees(99);
        dto.setMaxEmployees(99);
        dto.setIsVoluntary(true);
        dto.setIsTaxable(true);
        dto.setTaxCode("val-taxCode");
        dto.setHasWaitingPeriod(true);
        dto.setWaitingPeriodDays(99);
        dto.setEligibilityCheckRequired(true);
        dto.setEligibilityCriteria("val-eligibilityCriteria");
        dto.setTermsAndConditions("val-termsAndConditions");
        dto.setSummary("val-summary");
        dto.setCategory("val-category");
        dto.setPriority(99);
        dto.setContactInfo("val-contactInfo");
        dto.setWebsiteUrl("val-websiteUrl");
        dto.setBrochureUrl("val-brochureUrl");
        dto.setAnnualLimit(BigDecimal.ONE);
        dto.setNotes("val-notes");
        dto.setIsActive(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-planCode", dto.getPlanCode());
        assertEquals("val-planName", dto.getPlanName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-providerId", dto.getProviderId());
        assertEquals("val-providerName", dto.getProviderName());
        assertEquals(BigDecimal.ONE, dto.getEmployeeContribution());
        assertEquals(BigDecimal.ONE, dto.getEmployerContribution());
        assertEquals(BigDecimal.ONE, dto.getTotalCost());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpiryDate());
        assertEquals(99, dto.getEnrollmentWindowDays());
        assertTrue(dto.getRequiresEvidence());
        assertEquals(99, dto.getMinEmployees());
        assertEquals(99, dto.getMaxEmployees());
        assertTrue(dto.getIsVoluntary());
        assertTrue(dto.getIsTaxable());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertTrue(dto.getHasWaitingPeriod());
        assertEquals(99, dto.getWaitingPeriodDays());
        assertTrue(dto.getEligibilityCheckRequired());
        assertEquals("val-eligibilityCriteria", dto.getEligibilityCriteria());
        assertEquals("val-termsAndConditions", dto.getTermsAndConditions());
        assertEquals("val-summary", dto.getSummary());
        assertEquals("val-category", dto.getCategory());
        assertEquals(99, dto.getPriority());
        assertEquals("val-contactInfo", dto.getContactInfo());
        assertEquals("val-websiteUrl", dto.getWebsiteUrl());
        assertEquals("val-brochureUrl", dto.getBrochureUrl());
        assertEquals(BigDecimal.ONE, dto.getAnnualLimit());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateBenefitPlanRequest dto1 = CreateBenefitPlanRequest.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .planCode("test-planCode")
            .planName("test-planName")
            .description("test-description")
            .benefitType(BenefitType.HEALTH_INSURANCE)
            .status(BenefitStatus.ACTIVE)
            .providerId("test-providerId")
            .providerName("test-providerName")
            .employeeContribution(BigDecimal.TEN)
            .employerContribution(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency(DeductionFrequency.WEEKLY)
            .coverageOptions(Collections.emptyList())
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .enrollmentWindowDays(42)
            .requiresEvidence(true)
            .requiredDocuments(Collections.emptyList())
            .minEmployees(42)
            .maxEmployees(42)
            .isVoluntary(true)
            .isTaxable(true)
            .taxCode("test-taxCode")
            .hasWaitingPeriod(true)
            .waitingPeriodDays(42)
            .eligibilityCheckRequired(true)
            .eligibilityCriteria("test-eligibilityCriteria")
            .termsAndConditions("test-termsAndConditions")
            .summary("test-summary")
            .category("test-category")
            .priority(42)
            .contactInfo("test-contactInfo")
            .websiteUrl("test-websiteUrl")
            .brochureUrl("test-brochureUrl")
            .coveredServices(Collections.emptyList())
            .excludedServices(Collections.emptyList())
            .annualLimit(BigDecimal.TEN)
            .notes("test-notes")
            .isActive(true)
            .build();
        CreateBenefitPlanRequest dto2 = CreateBenefitPlanRequest.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .planCode("test-planCode")
            .planName("test-planName")
            .description("test-description")
            .benefitType(BenefitType.HEALTH_INSURANCE)
            .status(BenefitStatus.ACTIVE)
            .providerId("test-providerId")
            .providerName("test-providerName")
            .employeeContribution(BigDecimal.TEN)
            .employerContribution(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency(DeductionFrequency.WEEKLY)
            .coverageOptions(Collections.emptyList())
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .enrollmentWindowDays(42)
            .requiresEvidence(true)
            .requiredDocuments(Collections.emptyList())
            .minEmployees(42)
            .maxEmployees(42)
            .isVoluntary(true)
            .isTaxable(true)
            .taxCode("test-taxCode")
            .hasWaitingPeriod(true)
            .waitingPeriodDays(42)
            .eligibilityCheckRequired(true)
            .eligibilityCriteria("test-eligibilityCriteria")
            .termsAndConditions("test-termsAndConditions")
            .summary("test-summary")
            .category("test-category")
            .priority(42)
            .contactInfo("test-contactInfo")
            .websiteUrl("test-websiteUrl")
            .brochureUrl("test-brochureUrl")
            .coveredServices(Collections.emptyList())
            .excludedServices(Collections.emptyList())
            .annualLimit(BigDecimal.TEN)
            .notes("test-notes")
            .isActive(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreateBenefitPlanRequest dto = CreateBenefitPlanRequest.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .planCode("test-planCode")
            .planName("test-planName")
            .description("test-description")
            .benefitType(BenefitType.HEALTH_INSURANCE)
            .status(BenefitStatus.ACTIVE)
            .providerId("test-providerId")
            .providerName("test-providerName")
            .employeeContribution(BigDecimal.TEN)
            .employerContribution(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency(DeductionFrequency.WEEKLY)
            .coverageOptions(Collections.emptyList())
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .enrollmentWindowDays(42)
            .requiresEvidence(true)
            .requiredDocuments(Collections.emptyList())
            .minEmployees(42)
            .maxEmployees(42)
            .isVoluntary(true)
            .isTaxable(true)
            .taxCode("test-taxCode")
            .hasWaitingPeriod(true)
            .waitingPeriodDays(42)
            .eligibilityCheckRequired(true)
            .eligibilityCriteria("test-eligibilityCriteria")
            .termsAndConditions("test-termsAndConditions")
            .summary("test-summary")
            .category("test-category")
            .priority(42)
            .contactInfo("test-contactInfo")
            .websiteUrl("test-websiteUrl")
            .brochureUrl("test-brochureUrl")
            .coveredServices(Collections.emptyList())
            .excludedServices(Collections.emptyList())
            .annualLimit(BigDecimal.TEN)
            .notes("test-notes")
            .isActive(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}