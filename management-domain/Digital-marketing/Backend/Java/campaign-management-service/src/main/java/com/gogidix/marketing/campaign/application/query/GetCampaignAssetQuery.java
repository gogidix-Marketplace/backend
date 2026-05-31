package com.gogidix.marketing.campaign.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCampaignAssetQuery {

    private String tenantId;
    private String id;
}
