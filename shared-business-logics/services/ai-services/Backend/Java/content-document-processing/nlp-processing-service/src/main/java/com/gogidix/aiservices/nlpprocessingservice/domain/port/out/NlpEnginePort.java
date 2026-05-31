package com.gogidix.aiservices.nlpprocessingservice.domain.port.out;

import com.gogidix.aiservices.nlpprocessingservice.domain.aggregate.TextAnalysis;
import com.gogidix.aiservices.nlpprocessingservice.domain.model.LanguageCode;

import java.util.List;
import java.util.Map;

public interface NlpEnginePort {
    LanguageCode detectLanguage(String text);
    List<TextAnalysis.Entity> extractEntities(String text, LanguageCode language);
    TextAnalysis.SentimentResult analyzeSentiment(String text, LanguageCode language);
    Map<String, Double> categorizeText(String text, LanguageCode language);
    List<TextAnalysis.Keyword> extractKeywords(String text, LanguageCode language);
    String summarize(String text, LanguageCode language, double ratio);
}
