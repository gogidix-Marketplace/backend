package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.SearchBenefitPlansRequest;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitStatus;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitType;
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
class SearchBenefitPlansRequestTest {

        @Test
    void testBuilder() {
        SearchBenefitPlansRequest dto = SearchBenefitPlansRequest.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .benefitType(BenefitType.HEALTH_INSURANCE)
            .status(BenefitStatus.ACTIVE)
            .category("test-category")
            .isVoluntary(true)
            .isActive(true)
            .effectiveOn(LocalDate.of(2025,1,15))
            .expiringBefore(LocalDate.of(2025,1,15))
            .expiringAfter(LocalDate.of(2025,1,15))
            .providerId("test-providerId")
            .planCodes(Collections.emptyList())
            .searchQuery("test-searchQuery")
            .page(42)
            .size(42)
            .sortBy("test-sortBy")
            .sortDirection("test-sortDirection")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-category", dto.getCategory());
        assertTrue(dto.getIsVoluntary());
        assertTrue(dto.getIsActive());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveOn());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpiringBefore());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpiringAfter());
        assertEquals("test-providerId", dto.getProviderId());
        assertEquals("test-searchQuery", dto.getSearchQuery());
        assertEquals(42, dto.getPage());
        assertEquals(42, dto.getSize());
        assertEquals("test-sortBy", dto.getSortBy());
        assertEquals("test-sortDirection", dto.getSortDirection());
    }

    @Test
    void testSettersAndGetters() {
        SearchBenefitPlansRequest dto = new SearchBenefitPlansRequest();
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setCategory("val-category");
        dto.setIsVoluntary(true);
        dto.setIsActive(true);
        dto.setEffectiveOn(LocalDate.of(2025,6,1));
        dto.setExpiringBefore(LocalDate.of(2025,6,1));
        dto.setExpiringAfter(LocalDate.of(2025,6,1));
        dto.setProviderId("val-providerId");
        dto.setSearchQuery("val-searchQuery");
        dto.setPage(99);
        dto.setSize(99);
        dto.setSortBy("val-sortBy");
        dto.setSortDirection("val-sortDirection");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-category", dto.getCategory());
        assertTrue(dto.getIsVoluntary());
        assertTrue(dto.getIsActive());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveOn());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpiringBefore());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpiringAfter());
        assertEquals("val-providerId", dto.getProviderId());
        assertEquals("val-searchQuery", dto.getSearchQuery());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortDirection", dto.getSortDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        SearchBenefitPlansRequest dto1 = SearchBenefitPlansRequest.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .benefitType(BenefitType.HEALTH_INSURANCE)
            .status(BenefitStatus.ACTIVE)
            .category("test-category")
            .isVoluntary(true)
            .isActive(true)
            .effectiveOn(LocalDate.of(2025,1,15))
            .expiringBefore(LocalDate.of(2025,1,15))
            .expiringAfter(LocalDate.of(2025,1,15))
            .providerId("test-providerId")
            .planCodes(Collections.emptyList())
            .searchQuery("test-searchQuery")
            .page(42)
            .size(42)
            .sortBy("test-sortBy")
            .sortDirection("test-sortDirection")
            .build();
        SearchBenefitPlansRequest dto2 = SearchBenefitPlansRequest.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .benefitType(BenefitType.HEALTH_INSURANCE)
            .status(BenefitStatus.ACTIVE)
            .category("test-category")
            .isVoluntary(true)
            .isActive(true)
            .effectiveOn(LocalDate.of(2025,1,15))
            .expiringBefore(LocalDate.of(2025,1,15))
            .expiringAfter(LocalDate.of(2025,1,15))
            .providerId("test-providerId")
            .planCodes(Collections.emptyList())
            .searchQuery("test-searchQuery")
            .page(42)
            .size(42)
            .sortBy("test-sortBy")
            .sortDirection("test-sortDirection")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SearchBenefitPlansRequest dto = SearchBenefitPlansRequest.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .benefitType(BenefitType.HEALTH_INSURANCE)
            .status(BenefitStatus.ACTIVE)
            .category("test-category")
            .isVoluntary(true)
            .isActive(true)
            .effectiveOn(LocalDate.of(2025,1,15))
            .expiringBefore(LocalDate.of(2025,1,15))
            .expiringAfter(LocalDate.of(2025,1,15))
            .providerId("test-providerId")
            .planCodes(Collections.emptyList())
            .searchQuery("test-searchQuery")
            .page(42)
            .size(42)
            .sortBy("test-sortBy")
            .sortDirection("test-sortDirection")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}