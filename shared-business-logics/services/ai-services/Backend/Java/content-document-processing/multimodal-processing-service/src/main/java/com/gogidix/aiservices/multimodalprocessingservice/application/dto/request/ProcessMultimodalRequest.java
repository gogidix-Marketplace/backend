package com.gogidix.aiservices.multimodalprocessingservice.application.dto.request;

import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentItem;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentModality;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.OutputFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ProcessMultimodalRequest(
        @NotEmpty(message = "Content items cannot be empty")
        @Valid
        List<ContentItemDto> contentItems,
        OutputFormat outputFormat
) {
    public record ContentItemDto(
            @NotNull(message = "Modality is required")
            ContentModality modality,
            @NotNull(message = "URL is required")
            String url,
            Map<String, Object> metadata
    ) {}
}
