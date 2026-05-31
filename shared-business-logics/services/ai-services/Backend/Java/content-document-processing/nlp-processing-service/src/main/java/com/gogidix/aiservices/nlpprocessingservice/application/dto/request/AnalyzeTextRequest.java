package com.gogidix.aiservices.nlpprocessingservice.application.dto.request;

import com.gogidix.aiservices.nlpprocessingservice.domain.model.LanguageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AnalyzeTextRequest(
        @NotBlank(message = "Text is required")
        String text,
        LanguageCode language,
        @NotNull(message = "Features are required")
        FeatureConfig features
) {
    public AnalyzeTextRequest {
        if (features == null) {
            features = FeatureConfig.builder().build();
        }
    }

    @Builder
    public record FeatureConfig(
            boolean entities,
            boolean sentiment,
            boolean categories,
            boolean keywords
    ) {
        public FeatureConfig {
            if (!entities && !sentiment && !categories && !keywords) {
                // Default to all features if none specified
                entities = true;
                sentiment = true;
                categories = true;
                keywords = true;
            }
        }
    }
}
