package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitProviderResponse;
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
class BenefitProviderResponseTest {

        @Test
    void testBuilder() {
        BenefitProviderResponse dto = BenefitProviderResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .providerCode("test-providerCode")
            .providerName("test-providerName")
            .description("test-description")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .website("test-website")
            .address("test-address")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .countryCode("test-countryCode")
            .contractStartDate(LocalDate.of(2025,1,15))
            .contractEndDate(LocalDate.of(2025,1,15))
            .accountNumber("test-accountNumber")
            .paymentTerms("test-paymentTerms")
            .isActive(true)
            .notes("test-notes")
            .supportedPlanTypes(Collections.emptyList())
            .claimsContactEmail("test-claimsContactEmail")
            .claimsContactPhone("test-claimsContactPhone")
            .claimsPortalUrl("test-claimsPortalUrl")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-providerCode", dto.getProviderCode());
        assertEquals("test-providerName", dto.getProviderName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-contactEmail", dto.getContactEmail());
        assertEquals("test-contactPhone", dto.getContactPhone());
        assertEquals("test-website", dto.getWebsite());
        assertEquals("test-address", dto.getAddress());
        assertEquals("test-city", dto.getCity());
        assertEquals("test-state", dto.getState());
        assertEquals("test-postalCode", dto.getPostalCode());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals(LocalDate.of(2025,1,15), dto.getContractStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getContractEndDate());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-paymentTerms", dto.getPaymentTerms());
        assertTrue(dto.getIsActive());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-claimsContactEmail", dto.getClaimsContactEmail());
        assertEquals("test-claimsContactPhone", dto.getClaimsContactPhone());
        assertEquals("test-claimsPortalUrl", dto.getClaimsPortalUrl());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        BenefitProviderResponse dto = new BenefitProviderResponse();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setProviderCode("val-providerCode");
        dto.setProviderName("val-providerName");
        dto.setDescription("val-description");
        dto.setContactEmail("val-contactEmail");
        dto.setContactPhone("val-contactPhone");
        dto.setWebsite("val-website");
        dto.setAddress("val-address");
        dto.setCity("val-city");
        dto.setState("val-state");
        dto.setPostalCode("val-postalCode");
        dto.setCountryCode("val-countryCode");
        dto.setContractStartDate(LocalDate.of(2025,6,1));
        dto.setContractEndDate(LocalDate.of(2025,6,1));
        dto.setAccountNumber("val-accountNumber");
        dto.setPaymentTerms("val-paymentTerms");
        dto.setIsActive(true);
        dto.setNotes("val-notes");
        dto.setClaimsContactEmail("val-claimsContactEmail");
        dto.setClaimsContactPhone("val-claimsContactPhone");
        dto.setClaimsPortalUrl("val-claimsPortalUrl");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-providerCode", dto.getProviderCode());
        assertEquals("val-providerName", dto.getProviderName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-contactEmail", dto.getContactEmail());
        assertEquals("val-contactPhone", dto.getContactPhone());
        assertEquals("val-website", dto.getWebsite());
        assertEquals("val-address", dto.getAddress());
        assertEquals("val-city", dto.getCity());
        assertEquals("val-state", dto.getState());
        assertEquals("val-postalCode", dto.getPostalCode());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals(LocalDate.of(2025,6,1), dto.getContractStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getContractEndDate());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertTrue(dto.getIsActive());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-claimsContactEmail", dto.getClaimsContactEmail());
        assertEquals("val-claimsContactPhone", dto.getClaimsContactPhone());
        assertEquals("val-claimsPortalUrl", dto.getClaimsPortalUrl());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitProviderResponse dto1 = BenefitProviderResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .providerCode("test-providerCode")
            .providerName("test-providerName")
            .description("test-description")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .website("test-website")
            .address("test-address")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .countryCode("test-countryCode")
            .contractStartDate(LocalDate.of(2025,1,15))
            .contractEndDate(LocalDate.of(2025,1,15))
            .accountNumber("test-accountNumber")
            .paymentTerms("test-paymentTerms")
            .isActive(true)
            .notes("test-notes")
            .supportedPlanTypes(Collections.emptyList())
            .claimsContactEmail("test-claimsContactEmail")
            .claimsContactPhone("test-claimsContactPhone")
            .claimsPortalUrl("test-claimsPortalUrl")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedBy("test-updatedBy")
            .build();
        BenefitProviderResponse dto2 = BenefitProviderResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .providerCode("test-providerCode")
            .providerName("test-providerName")
            .description("test-description")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .website("test-website")
            .address("test-address")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .countryCode("test-countryCode")
            .contractStartDate(LocalDate.of(2025,1,15))
            .contractEndDate(LocalDate.of(2025,1,15))
            .accountNumber("test-accountNumber")
            .paymentTerms("test-paymentTerms")
            .isActive(true)
            .notes("test-notes")
            .supportedPlanTypes(Collections.emptyList())
            .claimsContactEmail("test-claimsContactEmail")
            .claimsContactPhone("test-claimsContactPhone")
            .claimsPortalUrl("test-claimsPortalUrl")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitProviderResponse dto = BenefitProviderResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .providerCode("test-providerCode")
            .providerName("test-providerName")
            .description("test-description")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .website("test-website")
            .address("test-address")
            .city("test-city")
            .state("test-state")
            .postalCode("test-postalCode")
            .countryCode("test-countryCode")
            .contractStartDate(LocalDate.of(2025,1,15))
            .contractEndDate(LocalDate.of(2025,1,15))
            .accountNumber("test-accountNumber")
            .paymentTerms("test-paymentTerms")
            .isActive(true)
            .notes("test-notes")
            .supportedPlanTypes(Collections.emptyList())
            .claimsContactEmail("test-claimsContactEmail")
            .claimsContactPhone("test-claimsContactPhone")
            .claimsPortalUrl("test-claimsPortalUrl")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}