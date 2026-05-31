package com.gogidix.digitalmarketing.countrymarketingdashboard.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCampaignCommand {

    private String id;
    private String tenantId;
}
