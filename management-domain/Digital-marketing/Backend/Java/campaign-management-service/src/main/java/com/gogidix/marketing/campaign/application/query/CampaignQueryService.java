package com.gogidix.marketing.campaign.application.query;

import com.gogidix.marketing.campaign.application.dto.CampaignDashboardDTO;
import com.gogidix.marketing.campaign.application.dto.CampaignResponseDTO;
import com.gogidix.marketing.campaign.domain.model.Campaign;
import com.gogidix.marketing.campaign.domain.model.CampaignChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface CampaignQueryService {

    CampaignResponseDTO getCampaignDetailById(String id);

    List<Campaign> getAllCampaigns();

    Page<Campaign> getCampaignsPaginated(Pageable pageable);

    List<Campaign> getCampaignsByStatus(String status);

    List<Campaign> getCampaignsByType(String type);

    List<Campaign> getCampaignsByScope(String scope);

    List<Campaign> getActiveCampaigns();

    List<Campaign> getDraftCampaigns();

    List<Campaign> getScheduledCampaigns();

    List<Campaign> getCampaignsByCountry(String country);

    List<Campaign> getCampaignsByRegion(String region);

    List<Campaign> getCampaignsByOwner(String owner);

    List<Campaign> getSubCampaigns(String parentId);

    List<Campaign> getTemplateCampaigns();

    List<Campaign> searchCampaigns(String term);

    List<Campaign> getCampaignsPendingApproval();

    List<Campaign> getCampaignsByApprovalStatus(String status);

    List<Campaign> getCampaignsRunningInDateRange(Instant start, Instant end);

    List<Campaign> getCampaignsWithBudgetAlerts(BigDecimal threshold);

    CampaignDashboardDTO getCampaignDashboard();

    List<CampaignChannel> getCampaignChannels(String campaignId);
}
