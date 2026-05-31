package com.gogidix.marketing.campaign.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * CreateCampaignRequest - DTO for creating a new campaign
 *
 * <p>Contains all fields required to create a new marketing campaign.</p>
 */
public class CreateCampaignRequest {

    @NotBlank(message = "Campaign name is required")
    @Size(min = 2, max = 200, message = "Campaign name must be between 2 and 200 characters")
    private String name;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotBlank(message = "Campaign type is required")
    private String campaignType;

    private String scope = "LOCAL";

    private List<String> regions;

    private List<String> countries;

    private Instant startDate;

    private Instant endDate;

    private BigDecimal totalBudget;

    private String currency = "USD";

    private String targetAudienceId;

    private List<String> channels;

    private String objective;

    private Map<String, BigDecimal> kpis;

    private String owner;

    private List<String> teamMembers;

    private List<String> tags;

    private Map<String, Object> metadata;

    private String priority = "MEDIUM";

    private Boolean isTemplate = false;

    private String parentCampaignId;

    private String templateId;

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
}
