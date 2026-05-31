package com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ProcessingStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record ProcessingStatusResponse(
        String processingId,
        ProcessingStatus status,
        int progress,
        int pagesProcessed,
        double confidence,
        Map<String, Object> extractedData,
        String errorMessage,
        Instant createdAt,
        Instant updatedAt
) {
}
