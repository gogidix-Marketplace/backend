package com.gogidix.aiservices.nlpprocessingservice.application.dto.request;

import com.gogidix.aiservices.nlpprocessingservice.domain.model.LanguageCode;
import jakarta.validation.constraints.NotBlank;

public record SummarizeTextRequest(
        @NotBlank(message = "Text is required")
        String text,
        LanguageCode language,
        double ratio
) {
    public SummarizeTextRequest {
        if (language == null) {
            language = LanguageCode.AUTO;
        }
        if (ratio == 0.0) {
            ratio = 0.3;
        }
    }

    public SummarizeTextRequest(String text, LanguageCode language) {
        this(text, language, 0.3);
    }

    public SummarizeTextRequest(String text) {
        this(text, LanguageCode.AUTO, 0.3);
    }
}
