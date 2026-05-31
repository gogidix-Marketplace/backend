package com.gogidix.digitalmarketing.countrymarketingdashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCampaignQuery {

    private String tenantId;
    private String id;
}
