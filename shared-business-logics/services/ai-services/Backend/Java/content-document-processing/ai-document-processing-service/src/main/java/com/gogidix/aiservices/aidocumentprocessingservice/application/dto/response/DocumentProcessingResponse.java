package com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ProcessingStatus;
import lombok.Builder;

import java.util.Map;

@Builder
public record DocumentProcessingResponse(
        String processingId,
        ProcessingStatus status,
        Map<String, Object> extractedData,
        double confidence,
        int pagesProcessed
) {
}
