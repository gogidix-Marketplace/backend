package com.gogidix.aiservices.leadgenerationaiservice.application.dto.request;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadLossReason;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLeadStatusRequest {
    @NotNull(message = "Lead ID is required")
    private String leadId;

    @NotNull(message = "Status is required")
    private LeadStatus status;

    private LeadLossReason lossReason;
}
