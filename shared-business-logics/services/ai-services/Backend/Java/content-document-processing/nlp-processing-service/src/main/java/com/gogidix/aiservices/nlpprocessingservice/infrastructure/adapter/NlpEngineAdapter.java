package com.gogidix.aiservices.nlpprocessingservice.infrastructure.adapter;

import com.gogidix.aiservices.nlpprocessingservice.domain.aggregate.TextAnalysis;
import com.gogidix.aiservices.nlpprocessingservice.domain.model.*;
import com.gogidix.aiservices.nlpprocessingservice.domain.port.out.NlpEnginePort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In-memory implementation of NLP processing for testing.
 * In production, this would integrate with actual NLP models/services.
 */
@Component
public class NlpEngineAdapter implements NlpEnginePort {

    private static final Map<String, LanguageCode> LANGUAGE_PATTERNS = Map.ofEntries(
            Map.entry("english", LanguageCode.EN),
            Map.entry("spanish", LanguageCode.ES),
            Map.entry("french", LanguageCode.FR),
            Map.entry("german", LanguageCode.DE),
            Map.entry("chinese", LanguageCode.ZH),
            Map.entry("japanese", LanguageCode.JA),
            Map.entry("arabic", LanguageCode.AR)
    );

    private static final Map<String, List<String>> PERSON_NAMES = Map.ofEntries(
            Map.entry("john", List.of("John")),
            Map.entry("jane", List.of("Jane")),
            Map.entry("google", List.of("Google")),
            Map.entry("apple", List.of("Apple"))
    );

    private static final List<String> POSITIVE_WORDS = List.of(
            "love", "amazing", "excellent", "great", "wonderful", "fantastic"
    );

    private static final List<String> NEGATIVE_WORDS = List.of(
            "terrible", "hate", "awful", "bad", "horrible", "worst"
    );

    private static final List<String> TECH_KEYWORDS = List.of(
            "machine learning", "artificial intelligence", "ai", "technology", "software"
    );

    @Override
    public LanguageCode detectLanguage(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        // Use domain's language detection
        LanguageCode detected = LanguageCode.detectFromText(text);

        return detected != LanguageCode.UNKNOWN ? detected : LanguageCode.EN;
    }

    @Override
    public List<TextAnalysis.Entity> extractEntities(String text, LanguageCode language) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        List<TextAnalysis.Entity> entities = new ArrayList<>();

        // Extract person names
        for (Map.Entry<String, List<String>> entry : PERSON_NAMES.entrySet()) {
            for (String name : entry.getValue()) {
                Pattern pattern = Pattern.compile("\\b" + Pattern.quote(name) + "\\b", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    entities.add(new TextAnalysis.Entity(
                            name,
                            EntityType.PERSON,
                            matcher.start(),
                            matcher.end(),
                            0.9
                    ));
                }
            }
        }

        // Extract organizations (simple pattern)
        Pattern orgPattern = Pattern.compile("\\b(Google|Apple|Microsoft|Amazon|Meta|Tesla)\\b");
        Matcher orgMatcher = orgPattern.matcher(text);
        while (orgMatcher.find()) {
            entities.add(new TextAnalysis.Entity(
                    orgMatcher.group(),
                    EntityType.ORGANIZATION,
                    orgMatcher.start(),
                    orgMatcher.end(),
                    0.95
            ));
        }

        // Extract locations
        Pattern locPattern = Pattern.compile("\\b(Paris|London|New York|California|Tokyo|Berlin)\\b");
        Matcher locMatcher = locPattern.matcher(text);
        while (locMatcher.find()) {
            entities.add(new TextAnalysis.Entity(
                    locMatcher.group(),
                    EntityType.LOCATION,
                    locMatcher.start(),
                    locMatcher.end(),
                    0.92
            ));
        }

        // Extract dates
        Pattern datePattern = Pattern.compile("\\b(\\d{4}-\\d{2}-\\d{2}|\\d{1,2}/\\d{1,2}/\\d{4})\\b");
        Matcher dateMatcher = datePattern.matcher(text);
        while (dateMatcher.find()) {
            entities.add(new TextAnalysis.Entity(
                    dateMatcher.group(),
                    EntityType.DATE,
                    dateMatcher.start(),
                    dateMatcher.end(),
                    0.88
            ));
        }

        return entities;
    }

    @Override
    public TextAnalysis.SentimentResult analyzeSentiment(String text, LanguageCode language) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        String lowerText = text.toLowerCase();

        int positiveCount = 0;
        int negativeCount = 0;

        for (String word : POSITIVE_WORDS) {
            if (lowerText.contains(word)) {
                positiveCount++;
            }
        }

        for (String word : NEGATIVE_WORDS) {
            if (lowerText.contains(word)) {
                negativeCount++;
            }
        }

        double score;
        SentimentLabel label;

        if (positiveCount > negativeCount) {
            score = Math.min(0.3 + (positiveCount * 0.1), 1.0);
            label = SentimentLabel.POSITIVE;
        } else if (negativeCount > positiveCount) {
            score = Math.max(-0.3 - (negativeCount * 0.1), -1.0);
            label = SentimentLabel.NEGATIVE;
        } else {
            score = 0.0;
            label = SentimentLabel.NEUTRAL;
        }

        return new TextAnalysis.SentimentResult(label, score);
    }

    @Override
    public Map<String, Double> categorizeText(String text, LanguageCode language) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        Map<String, Double> categories = new HashMap<>();
        String lowerText = text.toLowerCase();

        // Technology category
        double techScore = 0.0;
        for (String keyword : TECH_KEYWORDS) {
            if (lowerText.contains(keyword)) {
                techScore += 0.3;
            }
        }
        if (techScore > 0) {
            categories.put("technology", Math.min(techScore, 1.0));
        }

        // Business category
        double businessScore = 0.0;
        if (lowerText.contains("business") || lowerText.contains("company") || lowerText.contains("market")) {
            businessScore = 0.7;
        }
        if (businessScore > 0) {
            categories.put("business", businessScore);
        }

        return categories;
    }

    @Override
    public List<TextAnalysis.Keyword> extractKeywords(String text, LanguageCode language) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        List<TextAnalysis.Keyword> keywords = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        // Simple keyword extraction based on word frequency and length
        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            if (word.length() > 3) {
                wordCount.merge(word.replaceAll("[^a-zA-Z]", ""), 1, Integer::sum);
            }
        }

        // Convert to keywords with relevance scores
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() >= 1) {
                double relevance = Math.min(0.5 + (entry.getValue() * 0.1), 1.0);
                keywords.add(new TextAnalysis.Keyword(entry.getKey(), relevance));
            }
        }

        // Sort by relevance descending
        keywords.sort((a, b) -> Double.compare(b.relevance(), a.relevance()));

        return keywords.stream().limit(10).toList();
    }

    @Override
    public String summarize(String text, LanguageCode language, double ratio) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        if (ratio < 0.1 || ratio > 0.9) {
            throw new IllegalArgumentException("Ratio must be between 0.1 and 0.9");
        }

        int targetLength = Math.max(10, (int) (text.length() * ratio));

        // Simple extractive summarization: take first and last sentences
        String[] sentences = text.split("(?<=[.!?])\\s+");
        StringBuilder summary = new StringBuilder();

        int currentLength = 0;
        for (int i = 0; i < sentences.length && currentLength < targetLength; i++) {
            if (sentences[i].trim().length() > 0) {
                summary.append(sentences[i].trim()).append(". ");
                currentLength += sentences[i].trim().length();
            }
        }

        String result = summary.toString().trim();

        // Ensure minimum length
        int minLength = text.length() / 10;
        if (result.length() < minLength && sentences.length > 1) {
            result = sentences[0].trim() + "...";
        }

        return result;
    }
}
