package com.gogidix.sales.crm.application.dto.response;

import com.gogidix.sales.crm.application.dto.response.CustomerResponseDto;
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
class CustomerResponseDtoTest {

        @Test
    void testBuilder() {
        CustomerResponseDto dto = CustomerResponseDto.builder()
                        .id("test-id")
            .customerId("test-customerId")
            .tenantId("test-tenantId")
            .accountNumber("test-accountNumber")
            .companyName("test-companyName")
            .industry("test-industry")
            .segment(CustomerResponseDto.CustomerSegmentDto.ENTERPRISE)
            .lifecycleStage(CustomerResponseDto.CustomerLifecycleStageDto.LEAD)
            .website("test-website")
            .description("test-description")
            .employeeCount(42)
            .annualRevenue(null)
            .leadSource("test-leadSource")
            .leadDate(LocalDate.of(2025,1,15))
            .convertedDate(LocalDate.of(2025,1,15))
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .territory("test-territory")
            .billingAddress(null)
            .shippingAddress(null)
            .phoneNumber("test-phoneNumber")
            .email("test-email")
            .isActive(true)
            .lastContactDate(LocalDate.of(2025,1,15))
            .nextFollowUpDate(LocalDate.of(2025,1,15))
            .totalInteractions(42)
            .totalDealValue(null)
            .openDealsCount(42)
            .parentAccountId("test-parentAccountId")
            .childAccountIds(Collections.emptyList())
            .accountType(CustomerResponseDto.AccountTypeDto.STRATEGIC)
            .taxId("test-taxId")
            .paymentTerms("test-paymentTerms")
            .currency("test-currency")
            .creditLimit(null)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .satisfactionScore(42)
            .churnDate(LocalDate.of(2025,1,15))
            .churnReason("test-churnReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-accountNumber", dto.getAccountNumber());
        assertEquals("test-companyName", dto.getCompanyName());
        assertEquals("test-industry", dto.getIndustry());
        assertEquals(CustomerResponseDto.CustomerSegmentDto.ENTERPRISE, dto.getSegment());
        assertEquals(CustomerResponseDto.CustomerLifecycleStageDto.LEAD, dto.getLifecycleStage());
        assertEquals("test-website", dto.getWebsite());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getEmployeeCount());
        assertEquals("test-leadSource", dto.getLeadSource());
        assertEquals(LocalDate.of(2025,1,15), dto.getLeadDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getConvertedDate());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertEquals("test-ownerName", dto.getOwnerName());
        assertEquals("test-territory", dto.getTerritory());
        assertEquals("test-phoneNumber", dto.getPhoneNumber());
        assertEquals("test-email", dto.getEmail());
        assertTrue(dto.getIsActive());
        assertEquals(LocalDate.of(2025,1,15), dto.getLastContactDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getNextFollowUpDate());
        assertEquals(42, dto.getTotalInteractions());
        assertEquals(42, dto.getOpenDealsCount());
        assertEquals("test-parentAccountId", dto.getParentAccountId());
        assertEquals(CustomerResponseDto.AccountTypeDto.STRATEGIC, dto.getAccountType());
        assertEquals("test-taxId", dto.getTaxId());
        assertEquals("test-paymentTerms", dto.getPaymentTerms());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(42, dto.getSatisfactionScore());
        assertEquals(LocalDate.of(2025,1,15), dto.getChurnDate());
        assertEquals("test-churnReason", dto.getChurnReason());
    }

    @Test
    void testSettersAndGetters() {
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId("val-id");
        dto.setCustomerId("val-customerId");
        dto.setTenantId("val-tenantId");
        dto.setAccountNumber("val-accountNumber");
        dto.setCompanyName("val-companyName");
        dto.setIndustry("val-industry");
        dto.setSegment(CustomerResponseDto.CustomerSegmentDto.ENTERPRISE);
        dto.setLifecycleStage(CustomerResponseDto.CustomerLifecycleStageDto.LEAD);
        dto.setWebsite("val-website");
        dto.setDescription("val-description");
        dto.setEmployeeCount(99);
        dto.setLeadSource("val-leadSource");
        dto.setLeadDate(LocalDate.of(2025,6,1));
        dto.setConvertedDate(LocalDate.of(2025,6,1));
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setTerritory("val-territory");
        dto.setPhoneNumber("val-phoneNumber");
        dto.setEmail("val-email");
        dto.setIsActive(true);
        dto.setLastContactDate(LocalDate.of(2025,6,1));
        dto.setNextFollowUpDate(LocalDate.of(2025,6,1));
        dto.setTotalInteractions(99);
        dto.setOpenDealsCount(99);
        dto.setParentAccountId("val-parentAccountId");
        dto.setAccountType(CustomerResponseDto.AccountTypeDto.STRATEGIC);
        dto.setTaxId("val-taxId");
        dto.setPaymentTerms("val-paymentTerms");
        dto.setCurrency("val-currency");
        dto.setNotes("val-notes");
        dto.setSatisfactionScore(99);
        dto.setChurnDate(LocalDate.of(2025,6,1));
        dto.setChurnReason("val-churnReason");
        assertEquals("val-id", dto.getId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-companyName", dto.getCompanyName());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals(CustomerResponseDto.CustomerSegmentDto.ENTERPRISE, dto.getSegment());
        assertEquals(CustomerResponseDto.CustomerLifecycleStageDto.LEAD, dto.getLifecycleStage());
        assertEquals("val-website", dto.getWebsite());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getEmployeeCount());
        assertEquals("val-leadSource", dto.getLeadSource());
        assertEquals(LocalDate.of(2025,6,1), dto.getLeadDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getConvertedDate());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-phoneNumber", dto.getPhoneNumber());
        assertEquals("val-email", dto.getEmail());
        assertTrue(dto.getIsActive());
        assertEquals(LocalDate.of(2025,6,1), dto.getLastContactDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getNextFollowUpDate());
        assertEquals(99, dto.getTotalInteractions());
        assertEquals(99, dto.getOpenDealsCount());
        assertEquals("val-parentAccountId", dto.getParentAccountId());
        assertEquals(CustomerResponseDto.AccountTypeDto.STRATEGIC, dto.getAccountType());
        assertEquals("val-taxId", dto.getTaxId());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(99, dto.getSatisfactionScore());
        assertEquals(LocalDate.of(2025,6,1), dto.getChurnDate());
        assertEquals("val-churnReason", dto.getChurnReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerResponseDto dto1 = CustomerResponseDto.builder()
                        .id("test-id")
            .customerId("test-customerId")
            .tenantId("test-tenantId")
            .accountNumber("test-accountNumber")
            .companyName("test-companyName")
            .industry("test-industry")
            .segment(CustomerResponseDto.CustomerSegmentDto.ENTERPRISE)
            .lifecycleStage(CustomerResponseDto.CustomerLifecycleStageDto.LEAD)
            .website("test-website")
            .description("test-description")
            .employeeCount(42)
            .annualRevenue(null)
            .leadSource("test-leadSource")
            .leadDate(LocalDate.of(2025,1,15))
            .convertedDate(LocalDate.of(2025,1,15))
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .territory("test-territory")
            .billingAddress(null)
            .shippingAddress(null)
            .phoneNumber("test-phoneNumber")
            .email("test-email")
            .isActive(true)
            .lastContactDate(LocalDate.of(2025,1,15))
            .nextFollowUpDate(LocalDate.of(2025,1,15))
            .totalInteractions(42)
            .totalDealValue(null)
            .openDealsCount(42)
            .parentAccountId("test-parentAccountId")
            .childAccountIds(Collections.emptyList())
            .accountType(CustomerResponseDto.AccountTypeDto.STRATEGIC)
            .taxId("test-taxId")
            .paymentTerms("test-paymentTerms")
            .currency("test-currency")
            .creditLimit(null)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .satisfactionScore(42)
            .churnDate(LocalDate.of(2025,1,15))
            .churnReason("test-churnReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CustomerResponseDto dto2 = CustomerResponseDto.builder()
                        .id("test-id")
            .customerId("test-customerId")
            .tenantId("test-tenantId")
            .accountNumber("test-accountNumber")
            .companyName("test-companyName")
            .industry("test-industry")
            .segment(CustomerResponseDto.CustomerSegmentDto.ENTERPRISE)
            .lifecycleStage(CustomerResponseDto.CustomerLifecycleStageDto.LEAD)
            .website("test-website")
            .description("test-description")
            .employeeCount(42)
            .annualRevenue(null)
            .leadSource("test-leadSource")
            .leadDate(LocalDate.of(2025,1,15))
            .convertedDate(LocalDate.of(2025,1,15))
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .territory("test-territory")
            .billingAddress(null)
            .shippingAddress(null)
            .phoneNumber("test-phoneNumber")
            .email("test-email")
            .isActive(true)
            .lastContactDate(LocalDate.of(2025,1,15))
            .nextFollowUpDate(LocalDate.of(2025,1,15))
            .totalInteractions(42)
            .totalDealValue(null)
            .openDealsCount(42)
            .parentAccountId("test-parentAccountId")
            .childAccountIds(Collections.emptyList())
            .accountType(CustomerResponseDto.AccountTypeDto.STRATEGIC)
            .taxId("test-taxId")
            .paymentTerms("test-paymentTerms")
            .currency("test-currency")
            .creditLimit(null)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .satisfactionScore(42)
            .churnDate(LocalDate.of(2025,1,15))
            .churnReason("test-churnReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CustomerResponseDto dto = CustomerResponseDto.builder()
                        .id("test-id")
            .customerId("test-customerId")
            .tenantId("test-tenantId")
            .accountNumber("test-accountNumber")
            .companyName("test-companyName")
            .industry("test-industry")
            .segment(CustomerResponseDto.CustomerSegmentDto.ENTERPRISE)
            .lifecycleStage(CustomerResponseDto.CustomerLifecycleStageDto.LEAD)
            .website("test-website")
            .description("test-description")
            .employeeCount(42)
            .annualRevenue(null)
            .leadSource("test-leadSource")
            .leadDate(LocalDate.of(2025,1,15))
            .convertedDate(LocalDate.of(2025,1,15))
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .territory("test-territory")
            .billingAddress(null)
            .shippingAddress(null)
            .phoneNumber("test-phoneNumber")
            .email("test-email")
            .isActive(true)
            .lastContactDate(LocalDate.of(2025,1,15))
            .nextFollowUpDate(LocalDate.of(2025,1,15))
            .totalInteractions(42)
            .totalDealValue(null)
            .openDealsCount(42)
            .parentAccountId("test-parentAccountId")
            .childAccountIds(Collections.emptyList())
            .accountType(CustomerResponseDto.AccountTypeDto.STRATEGIC)
            .taxId("test-taxId")
            .paymentTerms("test-paymentTerms")
            .currency("test-currency")
            .creditLimit(null)
            .tags(Collections.emptyList())
            .notes("test-notes")
            .satisfactionScore(42)
            .churnDate(LocalDate.of(2025,1,15))
            .churnReason("test-churnReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}