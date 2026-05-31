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
class BenefitPlanDetailResponse_ProviderDetailTest {

        @Test
    void testBuilder() {
        BenefitPlanDetailResponse.ProviderDetail dto = BenefitPlanDetailResponse.ProviderDetail.builder()
                        .providerId("test-providerId")
            .providerName("test-providerName")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .website("test-website")
            .address("test-address")
            .claimsContactEmail("test-claimsContactEmail")
            .claimsContactPhone("test-claimsContactPhone")
            .claimsPortalUrl("test-claimsPortalUrl")
            .build();
        assertNotNull(dto);
        assertEquals("test-providerId", dto.getProviderId());
        assertEquals("test-providerName", dto.getProviderName());
        assertEquals("test-contactEmail", dto.getContactEmail());
        assertEquals("test-contactPhone", dto.getContactPhone());
        assertEquals("test-website", dto.getWebsite());
        assertEquals("test-address", dto.getAddress());
        assertEquals("test-claimsContactEmail", dto.getClaimsContactEmail());
        assertEquals("test-claimsContactPhone", dto.getClaimsContactPhone());
        assertEquals("test-claimsPortalUrl", dto.getClaimsPortalUrl());
    }

    @Test
    void testSettersAndGetters() {
        BenefitPlanDetailResponse.ProviderDetail dto = new BenefitPlanDetailResponse.ProviderDetail();
        dto.setProviderId("val-providerId");
        dto.setProviderName("val-providerName");
        dto.setContactEmail("val-contactEmail");
        dto.setContactPhone("val-contactPhone");
        dto.setWebsite("val-website");
        dto.setAddress("val-address");
        dto.setClaimsContactEmail("val-claimsContactEmail");
        dto.setClaimsContactPhone("val-claimsContactPhone");
        dto.setClaimsPortalUrl("val-claimsPortalUrl");
        assertEquals("val-providerId", dto.getProviderId());
        assertEquals("val-providerName", dto.getProviderName());
        assertEquals("val-contactEmail", dto.getContactEmail());
        assertEquals("val-contactPhone", dto.getContactPhone());
        assertEquals("val-website", dto.getWebsite());
        assertEquals("val-address", dto.getAddress());
        assertEquals("val-claimsContactEmail", dto.getClaimsContactEmail());
        assertEquals("val-claimsContactPhone", dto.getClaimsContactPhone());
        assertEquals("val-claimsPortalUrl", dto.getClaimsPortalUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitPlanDetailResponse.ProviderDetail dto1 = BenefitPlanDetailResponse.ProviderDetail.builder()
                        .providerId("test-providerId")
            .providerName("test-providerName")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .website("test-website")
            .address("test-address")
            .claimsContactEmail("test-claimsContactEmail")
            .claimsContactPhone("test-claimsContactPhone")
            .claimsPortalUrl("test-claimsPortalUrl")
            .build();
        BenefitPlanDetailResponse.ProviderDetail dto2 = BenefitPlanDetailResponse.ProviderDetail.builder()
                        .providerId("test-providerId")
            .providerName("test-providerName")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .website("test-website")
            .address("test-address")
            .claimsContactEmail("test-claimsContactEmail")
            .claimsContactPhone("test-claimsContactPhone")
            .claimsPortalUrl("test-claimsPortalUrl")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitPlanDetailResponse.ProviderDetail dto = BenefitPlanDetailResponse.ProviderDetail.builder()
                        .providerId("test-providerId")
            .providerName("test-providerName")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .website("test-website")
            .address("test-address")
            .claimsContactEmail("test-claimsContactEmail")
            .claimsContactPhone("test-claimsContactPhone")
            .claimsPortalUrl("test-claimsPortalUrl")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}