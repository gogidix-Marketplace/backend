package com.gogidix.aiservices.aidocumentprocessingservice.application.dto.request;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.DocumentType;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ExtractionConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ProcessDocumentRequest(
        @NotBlank(message = "Document URL is required")
        String documentUrl,
        @NotNull(message = "Document type is required")
        DocumentType documentType,
        ExtractionConfig extractionConfig
) {
    public ProcessDocumentRequest {
        if (extractionConfig != null && extractionConfig.getFields() != null &&
                extractionConfig.getFields().size() > 5) {
            throw new IllegalArgumentException("Cannot extract more than 5 fields");
        }
    }
}
