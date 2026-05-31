package com.gogidix.sales.onboarding.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteOnboardingTemplateCommand {

    private String id;
    private String tenantId;
}
