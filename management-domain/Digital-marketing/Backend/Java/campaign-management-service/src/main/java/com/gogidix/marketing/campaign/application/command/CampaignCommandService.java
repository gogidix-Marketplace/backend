package com.gogidix.marketing.campaign.application.command;

import com.gogidix.marketing.campaign.application.dto.CreateCampaignChannelRequest;
import com.gogidix.marketing.campaign.application.dto.CreateCampaignRequest;
import com.gogidix.marketing.campaign.application.dto.UpdateCampaignRequest;
import com.gogidix.marketing.campaign.domain.model.Campaign;
import com.gogidix.marketing.campaign.domain.model.CampaignChannel;

import java.math.BigDecimal;

public interface CampaignCommandService {

    Campaign createCampaign(CreateCampaignRequest request);

    Campaign updateCampaign(String id, UpdateCampaignRequest request);

    void deleteCampaign(String id);

    Campaign activateCampaign(String id);

    Campaign pauseCampaign(String id);

    Campaign cancelCampaign(String id);

    Campaign completeCampaign(String id);

    Campaign approveCampaign(String id, String approvedBy);

    Campaign rejectCampaign(String id, String rejectedBy);

    Campaign recordSpending(String id, BigDecimal amount);

    Campaign addChannel(String id, String channel);

    Campaign removeChannel(String id, String channel);

    CampaignChannel createCampaignChannel(CreateCampaignChannelRequest request);
}
