package com.gogidix.marketing.campaign.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.Map;

/**
 * CreateCampaignChannelRequest - DTO for creating a campaign channel
 */
public class CreateCampaignChannelRequest {

    @NotBlank(message = "Campaign ID is required")
    private String campaignId;

    @NotBlank(message = "Channel type is required")
    private String type;

    private String platform;

    private Map<String, Object> config;

    private String status = "PENDING";

    private Instant scheduledAt;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(Instant scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
