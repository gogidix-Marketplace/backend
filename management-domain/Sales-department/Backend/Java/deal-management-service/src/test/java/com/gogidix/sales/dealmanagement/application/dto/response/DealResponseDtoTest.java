package com.gogidix.sales.dealmanagement.application.dto.response;

import com.gogidix.sales.dealmanagement.application.dto.response.DealResponseDto;
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
class DealResponseDtoTest {

        @Test
    void testBuilder() {
        DealResponseDto dto = DealResponseDto.builder()
                        .id("test-id")
            .dealId("test-dealId")
            .tenantId("test-tenantId")
            .dealName("test-dealName")
            .dealCode("test-dealCode")
            .stage(DealResponseDto.DealStageDto.LEAD)
            .stageOrder(42)
            .probability(42)
            .amount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .currency("test-currency")
            .accountId("test-accountId")
            .accountName("test-accountName")
            .contactId("test-contactId")
            .contactName("test-contactName")
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .teamMemberIds(Collections.emptyList())
            .priority(DealResponseDto.DealPriorityDto.LOW)
            .status(DealResponseDto.DealStatusDto.OPEN)
            .approvalStatus(DealResponseDto.ApprovalStatusDto.NOT_REQUIRED)
            .approvalRequired(true)
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .actualCloseDate(LocalDate.of(2025,1,15))
            .createdDate(LocalDate.of(2025,1,15))
            .dealDurationDays(42)
            .source("test-source")
            .campaign("test-campaign")
            .leadSource("test-leadSource")
            .description("test-description")
            .nextSteps("test-nextSteps")
            .lossReason("test-lossReason")
            .lossReasonDetails("test-lossReasonDetails")
            .products(Collections.emptyList())
            .activities(Collections.emptyList())
            .competitors(Collections.emptyList())
            .tags(Collections.emptyList())
            .region("test-region")
            .industry("test-industry")
            .segment("test-segment")
            .territory("test-territory")
            .contractType("test-contractType")
            .contractLengthMonths(42)
            .renewal(true)
            .renewalDealId("test-renewalDealId")
            .discountAmount(BigDecimal.TEN)
            .discountPercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-dealId", dto.getDealId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-dealName", dto.getDealName());
        assertEquals("test-dealCode", dto.getDealCode());
        assertEquals(DealResponseDto.DealStageDto.LEAD, dto.getStage());
        assertEquals(42, dto.getStageOrder());
        assertEquals(42, dto.getProbability());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getWeightedAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-accountId", dto.getAccountId());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals("test-contactId", dto.getContactId());
        assertEquals("test-contactName", dto.getContactName());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertEquals("test-ownerName", dto.getOwnerName());
        assertEquals(DealResponseDto.DealPriorityDto.LOW, dto.getPriority());
        assertEquals(DealResponseDto.DealStatusDto.OPEN, dto.getStatus());
        assertEquals(DealResponseDto.ApprovalStatusDto.NOT_REQUIRED, dto.getApprovalStatus());
        assertTrue(dto.getApprovalRequired());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpectedCloseDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getActualCloseDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getCreatedDate());
        assertEquals(42, dto.getDealDurationDays());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-campaign", dto.getCampaign());
        assertEquals("test-leadSource", dto.getLeadSource());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-nextSteps", dto.getNextSteps());
        assertEquals("test-lossReason", dto.getLossReason());
        assertEquals("test-lossReasonDetails", dto.getLossReasonDetails());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-industry", dto.getIndustry());
        assertEquals("test-segment", dto.getSegment());
        assertEquals("test-territory", dto.getTerritory());
        assertEquals("test-contractType", dto.getContractType());
        assertEquals(42, dto.getContractLengthMonths());
        assertTrue(dto.getRenewal());
        assertEquals("test-renewalDealId", dto.getRenewalDealId());
        assertEquals(BigDecimal.TEN, dto.getDiscountAmount());
        assertEquals(BigDecimal.TEN, dto.getDiscountPercentage());
    }

    @Test
    void testSettersAndGetters() {
        DealResponseDto dto = new DealResponseDto();
        dto.setId("val-id");
        dto.setDealId("val-dealId");
        dto.setTenantId("val-tenantId");
        dto.setDealName("val-dealName");
        dto.setDealCode("val-dealCode");
        dto.setStage(DealResponseDto.DealStageDto.LEAD);
        dto.setStageOrder(99);
        dto.setProbability(99);
        dto.setAmount(BigDecimal.ONE);
        dto.setWeightedAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setAccountId("val-accountId");
        dto.setAccountName("val-accountName");
        dto.setContactId("val-contactId");
        dto.setContactName("val-contactName");
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setPriority(DealResponseDto.DealPriorityDto.LOW);
        dto.setStatus(DealResponseDto.DealStatusDto.OPEN);
        dto.setApprovalStatus(DealResponseDto.ApprovalStatusDto.NOT_REQUIRED);
        dto.setApprovalRequired(true);
        dto.setApprovedBy("val-approvedBy");
        dto.setExpectedCloseDate(LocalDate.of(2025,6,1));
        dto.setActualCloseDate(LocalDate.of(2025,6,1));
        dto.setCreatedDate(LocalDate.of(2025,6,1));
        dto.setDealDurationDays(99);
        dto.setSource("val-source");
        dto.setCampaign("val-campaign");
        dto.setLeadSource("val-leadSource");
        dto.setDescription("val-description");
        dto.setNextSteps("val-nextSteps");
        dto.setLossReason("val-lossReason");
        dto.setLossReasonDetails("val-lossReasonDetails");
        dto.setRegion("val-region");
        dto.setIndustry("val-industry");
        dto.setSegment("val-segment");
        dto.setTerritory("val-territory");
        dto.setContractType("val-contractType");
        dto.setContractLengthMonths(99);
        dto.setRenewal(true);
        dto.setRenewalDealId("val-renewalDealId");
        dto.setDiscountAmount(BigDecimal.ONE);
        dto.setDiscountPercentage(BigDecimal.ONE);
        assertEquals("val-id", dto.getId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealName", dto.getDealName());
        assertEquals("val-dealCode", dto.getDealCode());
        assertEquals(DealResponseDto.DealStageDto.LEAD, dto.getStage());
        assertEquals(99, dto.getStageOrder());
        assertEquals(99, dto.getProbability());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getWeightedAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals("val-contactId", dto.getContactId());
        assertEquals("val-contactName", dto.getContactName());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals(DealResponseDto.DealPriorityDto.LOW, dto.getPriority());
        assertEquals(DealResponseDto.DealStatusDto.OPEN, dto.getStatus());
        assertEquals(DealResponseDto.ApprovalStatusDto.NOT_REQUIRED, dto.getApprovalStatus());
        assertTrue(dto.getApprovalRequired());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpectedCloseDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getActualCloseDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getCreatedDate());
        assertEquals(99, dto.getDealDurationDays());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-campaign", dto.getCampaign());
        assertEquals("val-leadSource", dto.getLeadSource());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-nextSteps", dto.getNextSteps());
        assertEquals("val-lossReason", dto.getLossReason());
        assertEquals("val-lossReasonDetails", dto.getLossReasonDetails());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-segment", dto.getSegment());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-contractType", dto.getContractType());
        assertEquals(99, dto.getContractLengthMonths());
        assertTrue(dto.getRenewal());
        assertEquals("val-renewalDealId", dto.getRenewalDealId());
        assertEquals(BigDecimal.ONE, dto.getDiscountAmount());
        assertEquals(BigDecimal.ONE, dto.getDiscountPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        DealResponseDto dto1 = DealResponseDto.builder()
                        .id("test-id")
            .dealId("test-dealId")
            .tenantId("test-tenantId")
            .dealName("test-dealName")
            .dealCode("test-dealCode")
            .stage(DealResponseDto.DealStageDto.LEAD)
            .stageOrder(42)
            .probability(42)
            .amount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .currency("test-currency")
            .accountId("test-accountId")
            .accountName("test-accountName")
            .contactId("test-contactId")
            .contactName("test-contactName")
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .teamMemberIds(Collections.emptyList())
            .priority(DealResponseDto.DealPriorityDto.LOW)
            .status(DealResponseDto.DealStatusDto.OPEN)
            .approvalStatus(DealResponseDto.ApprovalStatusDto.NOT_REQUIRED)
            .approvalRequired(true)
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .actualCloseDate(LocalDate.of(2025,1,15))
            .createdDate(LocalDate.of(2025,1,15))
            .dealDurationDays(42)
            .source("test-source")
            .campaign("test-campaign")
            .leadSource("test-leadSource")
            .description("test-description")
            .nextSteps("test-nextSteps")
            .lossReason("test-lossReason")
            .lossReasonDetails("test-lossReasonDetails")
            .products(Collections.emptyList())
            .activities(Collections.emptyList())
            .competitors(Collections.emptyList())
            .tags(Collections.emptyList())
            .region("test-region")
            .industry("test-industry")
            .segment("test-segment")
            .territory("test-territory")
            .contractType("test-contractType")
            .contractLengthMonths(42)
            .renewal(true)
            .renewalDealId("test-renewalDealId")
            .discountAmount(BigDecimal.TEN)
            .discountPercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        DealResponseDto dto2 = DealResponseDto.builder()
                        .id("test-id")
            .dealId("test-dealId")
            .tenantId("test-tenantId")
            .dealName("test-dealName")
            .dealCode("test-dealCode")
            .stage(DealResponseDto.DealStageDto.LEAD)
            .stageOrder(42)
            .probability(42)
            .amount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .currency("test-currency")
            .accountId("test-accountId")
            .accountName("test-accountName")
            .contactId("test-contactId")
            .contactName("test-contactName")
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .teamMemberIds(Collections.emptyList())
            .priority(DealResponseDto.DealPriorityDto.LOW)
            .status(DealResponseDto.DealStatusDto.OPEN)
            .approvalStatus(DealResponseDto.ApprovalStatusDto.NOT_REQUIRED)
            .approvalRequired(true)
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .actualCloseDate(LocalDate.of(2025,1,15))
            .createdDate(LocalDate.of(2025,1,15))
            .dealDurationDays(42)
            .source("test-source")
            .campaign("test-campaign")
            .leadSource("test-leadSource")
            .description("test-description")
            .nextSteps("test-nextSteps")
            .lossReason("test-lossReason")
            .lossReasonDetails("test-lossReasonDetails")
            .products(Collections.emptyList())
            .activities(Collections.emptyList())
            .competitors(Collections.emptyList())
            .tags(Collections.emptyList())
            .region("test-region")
            .industry("test-industry")
            .segment("test-segment")
            .territory("test-territory")
            .contractType("test-contractType")
            .contractLengthMonths(42)
            .renewal(true)
            .renewalDealId("test-renewalDealId")
            .discountAmount(BigDecimal.TEN)
            .discountPercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DealResponseDto dto = DealResponseDto.builder()
                        .id("test-id")
            .dealId("test-dealId")
            .tenantId("test-tenantId")
            .dealName("test-dealName")
            .dealCode("test-dealCode")
            .stage(DealResponseDto.DealStageDto.LEAD)
            .stageOrder(42)
            .probability(42)
            .amount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .currency("test-currency")
            .accountId("test-accountId")
            .accountName("test-accountName")
            .contactId("test-contactId")
            .contactName("test-contactName")
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .teamMemberIds(Collections.emptyList())
            .priority(DealResponseDto.DealPriorityDto.LOW)
            .status(DealResponseDto.DealStatusDto.OPEN)
            .approvalStatus(DealResponseDto.ApprovalStatusDto.NOT_REQUIRED)
            .approvalRequired(true)
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .actualCloseDate(LocalDate.of(2025,1,15))
            .createdDate(LocalDate.of(2025,1,15))
            .dealDurationDays(42)
            .source("test-source")
            .campaign("test-campaign")
            .leadSource("test-leadSource")
            .description("test-description")
            .nextSteps("test-nextSteps")
            .lossReason("test-lossReason")
            .lossReasonDetails("test-lossReasonDetails")
            .products(Collections.emptyList())
            .activities(Collections.emptyList())
            .competitors(Collections.emptyList())
            .tags(Collections.emptyList())
            .region("test-region")
            .industry("test-industry")
            .segment("test-segment")
            .territory("test-territory")
            .contractType("test-contractType")
            .contractLengthMonths(42)
            .renewal(true)
            .renewalDealId("test-renewalDealId")
            .discountAmount(BigDecimal.TEN)
            .discountPercentage(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}