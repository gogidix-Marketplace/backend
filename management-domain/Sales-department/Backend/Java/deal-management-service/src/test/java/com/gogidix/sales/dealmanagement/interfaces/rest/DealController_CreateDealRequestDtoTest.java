package com.gogidix.sales.dealmanagement.interfaces.rest;

import com.gogidix.sales.dealmanagement.domain.model.Deal;
import com.gogidix.sales.dealmanagement.interfaces.rest.DealController;
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
class DealController_CreateDealRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        DealController.CreateDealRequestDto dto = new DealController.CreateDealRequestDto();
        dto.setDealName("val-dealName");
        dto.setAccountId("val-accountId");
        dto.setAccountName("val-accountName");
        dto.setContactId("val-contactId");
        dto.setContactName("val-contactName");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setExpectedCloseDate(LocalDate.of(2025,6,1));
        dto.setSource("val-source");
        dto.setCampaign("val-campaign");
        dto.setLeadSource("val-leadSource");
        dto.setDescription("val-description");
        dto.setNextSteps("val-nextSteps");
        dto.setRegion("val-region");
        dto.setIndustry("val-industry");
        dto.setSegment("val-segment");
        dto.setTerritory("val-territory");
        dto.setContractType("val-contractType");
        dto.setContractLengthMonths(99);
        dto.setRenewal(true);
        dto.setRenewalDealId("val-renewalDealId");
        assertEquals("val-dealName", dto.getDealName());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals("val-contactId", dto.getContactId());
        assertEquals("val-contactName", dto.getContactName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpectedCloseDate());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-campaign", dto.getCampaign());
        assertEquals("val-leadSource", dto.getLeadSource());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-nextSteps", dto.getNextSteps());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-segment", dto.getSegment());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-contractType", dto.getContractType());
        assertEquals(99, dto.getContractLengthMonths());
        assertTrue(dto.getRenewal());
        assertEquals("val-renewalDealId", dto.getRenewalDealId());
    }

    @Test
    void testEqualsAndHashCode() {
        DealController.CreateDealRequestDto dto1 = new DealController.CreateDealRequestDto();
        DealController.CreateDealRequestDto dto2 = new DealController.CreateDealRequestDto();
        dto1.setDealName("test");
        dto1.setAccountId("test");
        dto1.setAccountName("test");
        dto1.setContactId("test");
        dto1.setContactName("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setStage(Deal.DealStage.LEAD);
        dto1.setOwnerId("test");
        dto1.setOwnerName("test");
        dto1.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto1.setPriority(Deal.DealPriority.LOW);
        dto1.setSource("test");
        dto1.setCampaign("test");
        dto1.setLeadSource("test");
        dto1.setDescription("test");
        dto1.setNextSteps("test");
        dto1.setRegion("test");
        dto1.setIndustry("test");
        dto1.setSegment("test");
        dto1.setTerritory("test");
        dto1.setContractType("test");
        dto1.setContractLengthMonths(42);
        dto1.setRenewal(true);
        dto1.setRenewalDealId("test");
        dto1.setTeamMemberIds(Collections.emptyList());
        dto1.setTags(Collections.emptyList());
        dto1.setProducts(Collections.emptyList());
        dto2.setDealName("test");
        dto2.setAccountId("test");
        dto2.setAccountName("test");
        dto2.setContactId("test");
        dto2.setContactName("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setStage(Deal.DealStage.LEAD);
        dto2.setOwnerId("test");
        dto2.setOwnerName("test");
        dto2.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto2.setPriority(Deal.DealPriority.LOW);
        dto2.setSource("test");
        dto2.setCampaign("test");
        dto2.setLeadSource("test");
        dto2.setDescription("test");
        dto2.setNextSteps("test");
        dto2.setRegion("test");
        dto2.setIndustry("test");
        dto2.setSegment("test");
        dto2.setTerritory("test");
        dto2.setContractType("test");
        dto2.setContractLengthMonths(42);
        dto2.setRenewal(true);
        dto2.setRenewalDealId("test");
        dto2.setTeamMemberIds(Collections.emptyList());
        dto2.setTags(Collections.emptyList());
        dto2.setProducts(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setDealName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealController.CreateDealRequestDto dto = new DealController.CreateDealRequestDto();
        dto.setDealName("test");
        dto.setAccountId("test");
        dto.setAccountName("test");
        dto.setContactId("test");
        dto.setContactName("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setStage(Deal.DealStage.LEAD);
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto.setPriority(Deal.DealPriority.LOW);
        dto.setSource("test");
        dto.setCampaign("test");
        dto.setLeadSource("test");
        dto.setDescription("test");
        dto.setNextSteps("test");
        dto.setRegion("test");
        dto.setIndustry("test");
        dto.setSegment("test");
        dto.setTerritory("test");
        dto.setContractType("test");
        dto.setContractLengthMonths(42);
        dto.setRenewal(true);
        dto.setRenewalDealId("test");
        dto.setTeamMemberIds(Collections.emptyList());
        dto.setTags(Collections.emptyList());
        dto.setProducts(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealController.CreateDealRequestDto dto = new DealController.CreateDealRequestDto();
        dto.setDealName("test");
        dto.setAccountId("test");
        dto.setAccountName("test");
        dto.setContactId("test");
        dto.setContactName("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setStage(Deal.DealStage.LEAD);
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto.setPriority(Deal.DealPriority.LOW);
        dto.setSource("test");
        dto.setCampaign("test");
        dto.setLeadSource("test");
        dto.setDescription("test");
        dto.setNextSteps("test");
        dto.setRegion("test");
        dto.setIndustry("test");
        dto.setSegment("test");
        dto.setTerritory("test");
        dto.setContractType("test");
        dto.setContractLengthMonths(42);
        dto.setRenewal(true);
        dto.setRenewalDealId("test");
        dto.setTeamMemberIds(Collections.emptyList());
        dto.setTags(Collections.emptyList());
        dto.setProducts(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}