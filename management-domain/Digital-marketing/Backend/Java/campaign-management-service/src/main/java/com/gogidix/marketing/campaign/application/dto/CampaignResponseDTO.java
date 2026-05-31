package com.gogidix.marketing.campaign.application.dto;

import com.gogidix.marketing.campaign.domain.model.Campaign;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * CampaignResponseDTO - Response DTO for campaign data
 *
 * <p>Contains campaign data for API responses.</p>
 */
public class CampaignResponseDTO {

    private String id;
    private String name;
    private String description;
    private String campaignType;
    private String scope;
    private List<String> regions;
    private List<String> countries;
    private String status;
    private Instant startDate;
    private Instant endDate;
    private BigDecimal totalBudget;
    private BigDecimal spentAmount;
    private BigDecimal remainingBudget;
    private BigDecimal budgetUtilization;
    private String currency;
    private String targetAudienceId;
    private List<String> channels;
    private String objective;
    private Map<String, BigDecimal> kpis;
    private String owner;
    private List<String> teamMembers;
    private List<String> tags;
    private Map<String, Object> metadata;
    private String priority;
    private Boolean isTemplate;
    private String parentCampaignId;
    private String templateId;
    private String approvalStatus;
    private String approvedBy;
    private Instant approvedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    public static CampaignResponseDTO fromEntity(Campaign campaign) {
        CampaignResponseDTO dto = new CampaignResponseDTO();
        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setDescription(campaign.getDescription());
        dto.setCampaignType(campaign.getCampaignType());
        dto.setScope(campaign.getScope());
        dto.setRegions(campaign.getRegions());
        dto.setCountries(campaign.getCountries());
        dto.setStatus(campaign.getStatus());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setTotalBudget(campaign.getTotalBudget());
        dto.setSpentAmount(campaign.getSpentAmount());
        dto.setRemainingBudget(campaign.getRemainingBudget());
        dto.setBudgetUtilization(campaign.getBudgetUtilization());
        dto.setCurrency(campaign.getCurrency());
        dto.setTargetAudienceId(campaign.getTargetAudienceId());
        dto.setChannels(campaign.getChannels());
        dto.setObjective(campaign.getObjective());
        dto.setKpis(campaign.getKpis());
        dto.setOwner(campaign.getOwner());
        dto.setTeamMembers(campaign.getTeamMembers());
        dto.setTags(campaign.getTags());
        dto.setMetadata(campaign.getMetadata());
        dto.setPriority(campaign.getPriority());
        dto.setIsTemplate(campaign.getIsTemplate());
        dto.setParentCampaignId(campaign.getParentCampaignId());
        dto.setTemplateId(campaign.getTemplateId());
        dto.setApprovalStatus(campaign.getApprovalStatus());
        dto.setApprovedBy(campaign.getApprovedBy());
        dto.setApprovedAt(campaign.getApprovedAt());
        dto.setCreatedAt(campaign.getCreatedAt());
        dto.setUpdatedAt(campaign.getUpdatedAt());
        dto.setCreatedBy(campaign.getCreatedBy());
        dto.setUpdatedBy(campaign.getUpdatedBy());
        return dto;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
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

    public BigDecimal getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(BigDecimal remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public BigDecimal getBudgetUtilization() {
        return budgetUtilization;
    }

    public void setBudgetUtilization(BigDecimal budgetUtilization) {
        this.budgetUtilization = budgetUtilization;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTargetAudienceId() {
        return targetAudienceId;
    }

    public void setTargetAudienceId(String targetAudienceId) {
        this.targetAudienceId = targetAudienceId;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public Map<String, BigDecimal> getKpis() {
        return kpis;
    }

    public void setKpis(Map<String, BigDecimal> kpis) {
        this.kpis = kpis;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public String getParentCampaignId() {
        return parentCampaignId;
    }

    public void setParentCampaignId(String parentCampaignId) {
        this.parentCampaignId = parentCampaignId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
