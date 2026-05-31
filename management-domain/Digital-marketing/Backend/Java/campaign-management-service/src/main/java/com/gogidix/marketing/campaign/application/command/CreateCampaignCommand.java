package com.gogidix.marketing.campaign.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCampaignCommand {

    private String id;
    private String tenantId;
}
