package com.gogidix.aiservices.aicontentgenerationservice.domain.port.out;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentTone;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentType;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GeneratedContent;

import java.util.List;

public interface ContentGenerationPort {
    GeneratedContent generateContent(String prompt, ContentType type, ContentTone tone, String language);

    List<GeneratedContent> generateVariations(String prompt, ContentType type, ContentTone tone,
                                             String language, int count);

    String optimizeContent(String content, ContentType type, String optimizationGoal);

    double calculateRelevanceScore(String content, String prompt);

    record GenerationRequest(String prompt, ContentType type, ContentTone tone, String language, int variations) {}
}
