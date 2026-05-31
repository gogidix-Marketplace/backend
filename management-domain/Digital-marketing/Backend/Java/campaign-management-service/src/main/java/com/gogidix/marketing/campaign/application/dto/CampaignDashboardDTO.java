package com.gogidix.marketing.campaign.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * CampaignDashboardDTO - Dashboard data for campaigns
 *
 * <p>Aggregated campaign metrics for dashboard display.</p>
 */
public class CampaignDashboardDTO {

    private long totalCampaigns;
    private long activeCampaigns;
    private long draftCampaigns;
    private long scheduledCampaigns;
    private long completedCampaigns;
    private long pausedCampaigns;
    private long cancelledCampaigns;
    private BigDecimal totalBudget;
    private BigDecimal totalSpent;
    private BigDecimal averageBudgetUtilization;
    private Map<String, Long> campaignsByType;
    private Map<String, Long> campaignsByStatus;
    private Map<String, BigDecimal> spendByChannel;
    private List<CampaignSummaryDTO> recentCampaigns;
    private List<CampaignSummaryDTO> campaignsNeedingAttention;
    private List<CampaignSummaryDTO> topPerformingCampaigns;

    public long getTotalCampaigns() {
        return totalCampaigns;
    }

    public void setTotalCampaigns(long totalCampaigns) {
        this.totalCampaigns = totalCampaigns;
    }

    public long getActiveCampaigns() {
        return activeCampaigns;
    }

    public void setActiveCampaigns(long activeCampaigns) {
        this.activeCampaigns = activeCampaigns;
    }

    public long getDraftCampaigns() {
        return draftCampaigns;
    }

    public void setDraftCampaigns(long draftCampaigns) {
        this.draftCampaigns = draftCampaigns;
    }

    public long getScheduledCampaigns() {
        return scheduledCampaigns;
    }

    public void setScheduledCampaigns(long scheduledCampaigns) {
        this.scheduledCampaigns = scheduledCampaigns;
    }

    public long getCompletedCampaigns() {
        return completedCampaigns;
    }

    public void setCompletedCampaigns(long completedCampaigns) {
        this.completedCampaigns = completedCampaigns;
    }

    public long getPausedCampaigns() {
        return pausedCampaigns;
    }

    public void setPausedCampaigns(long pausedCampaigns) {
        this.pausedCampaigns = pausedCampaigns;
    }

    public long getCancelledCampaigns() {
        return cancelledCampaigns;
    }

    public void setCancelledCampaigns(long cancelledCampaigns) {
        this.cancelledCampaigns = cancelledCampaigns;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public BigDecimal getAverageBudgetUtilization() {
        return averageBudgetUtilization;
    }

    public void setAverageBudgetUtilization(BigDecimal averageBudgetUtilization) {
        this.averageBudgetUtilization = averageBudgetUtilization;
    }

    public Map<String, Long> getCampaignsByType() {
        return campaignsByType;
    }

    public void setCampaignsByType(Map<String, Long> campaignsByType) {
        this.campaignsByType = campaignsByType;
    }

    public Map<String, Long> getCampaignsByStatus() {
        return campaignsByStatus;
    }

    public void setCampaignsByStatus(Map<String, Long> campaignsByStatus) {
        this.campaignsByStatus = campaignsByStatus;
    }

    public Map<String, BigDecimal> getSpendByChannel() {
        return spendByChannel;
    }

    public void setSpendByChannel(Map<String, BigDecimal> spendByChannel) {
        this.spendByChannel = spendByChannel;
    }

    public List<CampaignSummaryDTO> getRecentCampaigns() {
        return recentCampaigns;
    }

    public void setRecentCampaigns(List<CampaignSummaryDTO> recentCampaigns) {
        this.recentCampaigns = recentCampaigns;
    }

    public List<CampaignSummaryDTO> getCampaignsNeedingAttention() {
        return campaignsNeedingAttention;
    }

    public void setCampaignsNeedingAttention(List<CampaignSummaryDTO> campaignsNeedingAttention) {
        this.campaignsNeedingAttention = campaignsNeedingAttention;
    }

    public List<CampaignSummaryDTO> getTopPerformingCampaigns() {
        return topPerformingCampaigns;
    }

    public void setTopPerformingCampaigns(List<CampaignSummaryDTO> topPerformingCampaigns) {
        this.topPerformingCampaigns = topPerformingCampaigns;
    }

    /**
     * CampaignSummaryDTO - Summary data for a single campaign
     */
    public static class CampaignSummaryDTO {
        private String id;
        private String name;
        private String status;
        private String campaignType;
        private BigDecimal totalBudget;
        private BigDecimal spentAmount;
        private BigDecimal roi;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCampaignType() {
            return campaignType;
        }

        public void setCampaignType(String campaignType) {
            this.campaignType = campaignType;
        }

        public BigDecimal getTotalBudget() {
            return totalBudget;
        }

        public void setTotalBudget(BigDecimal totalBudget) {
            this.totalBudget = totalBudget;
        }

        public BigDecimal getSpentAmount() {
            return spentAmount;
        }

        public void setSpentAmount(BigDecimal spentAmount) {
            this.spentAmount = spentAmount;
        }

        public BigDecimal getRoi() {
            return roi;
        }

        public void setRoi(BigDecimal roi) {
            this.roi = roi;
        }
    }
}
