package com.gogidix.aiservices.leadgenerationaiservice.application.dto.response;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadTier;
import lombok.Builder;

@Builder
public record ScoreResponse(
        String leadId,
        Double score,
        LeadTier tier,
        String reason
) {
}
