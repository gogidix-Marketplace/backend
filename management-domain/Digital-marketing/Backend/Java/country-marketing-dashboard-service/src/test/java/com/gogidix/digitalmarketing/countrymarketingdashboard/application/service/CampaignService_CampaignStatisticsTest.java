package com.gogidix.digitalmarketing.countrymarketingdashboard.application.service;

import com.gogidix.digitalmarketing.countrymarketingdashboard.application.service.CampaignService;
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
class CampaignService_CampaignStatisticsTest {

        @Test
    void testSettersAndGetters() {
        CampaignService.CampaignStatistics dto = new CampaignService.CampaignStatistics();
        dto.setTotalCampaigns(99);
        assertEquals(99, dto.getTotalCampaigns());
    }

    @Test
    void testEqualsAndHashCode() {
        CampaignService.CampaignStatistics dto1 = new CampaignService.CampaignStatistics();
        CampaignService.CampaignStatistics dto2 = new CampaignService.CampaignStatistics();
        dto1.setTotalCampaigns(42);
        dto1.setActiveCampaigns(42L);
        dto1.setPausedCampaigns(42L);
        dto1.setCompletedCampaigns(42L);
        dto2.setTotalCampaigns(42);
        dto2.setActiveCampaigns(42L);
        dto2.setPausedCampaigns(42L);
        dto2.setCompletedCampaigns(42L);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTotalCampaigns(999);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CampaignService.CampaignStatistics dto = new CampaignService.CampaignStatistics();
        dto.setTotalCampaigns(42);
        dto.setActiveCampaigns(42L);
        dto.setPausedCampaigns(42L);
        dto.setCompletedCampaigns(42L);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CampaignService.CampaignStatistics dto = new CampaignService.CampaignStatistics();
        dto.setTotalCampaigns(42);
        dto.setActiveCampaigns(42L);
        dto.setPausedCampaigns(42L);
        dto.setCompletedCampaigns(42L);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}