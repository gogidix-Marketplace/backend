package com.gogidix.aiservices.leadgenerationaiservice.application.dto.response;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ConversionResponse(
        String leadId,
        LeadStatus status,
        Double value,
        String currency,
        Instant convertedAt
) {
}
