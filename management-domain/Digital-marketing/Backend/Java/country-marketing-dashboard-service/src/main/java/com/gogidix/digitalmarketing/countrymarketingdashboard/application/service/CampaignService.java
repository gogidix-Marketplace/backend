package com.gogidix.digitalmarketing.countrymarketingdashboard.application.service;

import com.gogidix.digitalmarketing.countrymarketingdashboard.application.dto.CampaignDTO;
import com.gogidix.digitalmarketing.countrymarketingdashboard.domain.model.Campaign;
import com.gogidix.digitalmarketing.countrymarketingdashboard.domain.repository.CampaignRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Campaign createCampaign(String tenantId, CampaignDTO.CreateCampaignRequest request) {
        Campaign campaign = Campaign.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .type(request.getType())
                .status("DRAFT")
                .budget(request.getBudget())
                .spent(BigDecimal.ZERO)
                .remainingBudget(request.getBudget())
                .country(request.getCountry())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        return campaignRepository.save(campaign);
    }

    public Optional<Campaign> getCampaign(String tenantId, String campaignId) {
        return campaignRepository.findById(campaignId);
    }

    public Campaign updateCampaign(String tenantId, String campaignId, CampaignDTO.UpdateCampaignRequest request) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
        if (request.getName() != null) campaign.setName(request.getName());
        if (request.getStatus() != null) campaign.setStatus(request.getStatus());
        return campaignRepository.save(campaign);
    }

    public Campaign launchCampaign(String tenantId, String campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
        campaign.setStatus("ACTIVE");
        return campaignRepository.save(campaign);
    }

    public Campaign pauseCampaign(String tenantId, String campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
        campaign.setStatus("PAUSED");
        return campaignRepository.save(campaign);
    }

    public void deleteCampaign(String tenantId, String campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
        campaignRepository.delete(campaign);
    }

    public Campaign recordSpend(String tenantId, String campaignId, BigDecimal amount) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
        campaign.setSpent(campaign.getSpent().add(amount));
        return campaignRepository.save(campaign);
    }

    public CampaignStatistics getCampaignStatistics(String tenantId, String country) {
        List<Campaign> all = campaignRepository.findCampaignsForDashboard(tenantId, country);
        long active = campaignRepository.countByTenantIdAndCountryAndStatus(tenantId, country, "ACTIVE");
        long paused = campaignRepository.countByTenantIdAndCountryAndStatus(tenantId, country, "PAUSED");
        long completed = campaignRepository.countByTenantIdAndCountryAndStatus(tenantId, country, "COMPLETED");
        return new CampaignStatistics(all.size(), active, paused, completed);
    }

    public Page<Campaign> getCampaigns(String tenantId, String country, Pageable pageable) {
        return campaignRepository.findByTenantIdAndCountry(tenantId, country, pageable);
    }

    public List<Campaign> getActiveCampaigns(String tenantId, String country) {
        return campaignRepository.findActiveCampaigns(tenantId, country, Instant.now());
    }

    public List<Campaign> getCampaignsByStatus(String tenantId, String country, String status) {
        return campaignRepository.findByTenantIdAndCountryAndStatus(tenantId, country, status);
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class CampaignStatistics {
        private int totalCampaigns;
        private long activeCampaigns;
        private long pausedCampaigns;
        private long completedCampaigns;
    }
}
