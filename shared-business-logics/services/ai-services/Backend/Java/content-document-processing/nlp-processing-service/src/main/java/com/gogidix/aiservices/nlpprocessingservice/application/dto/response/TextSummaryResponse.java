package com.gogidix.aiservices.nlpprocessingservice.application.dto.response;

import com.gogidix.aiservices.nlpprocessingservice.domain.model.LanguageCode;

public record TextSummaryResponse(
        String summary,
        int originalLength,
        int summaryLength,
        LanguageCode detectedLanguage
) {}
