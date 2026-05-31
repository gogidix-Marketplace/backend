package com.gogidix.aiservices.nlpprocessingservice.domain.aggregate;

import com.gogidix.aiservices.nlpprocessingservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TextAnalysis Aggregate Tests")
class TextAnalysisAggregateTest {

    private static final String SAMPLE_TEXT = "This is a sample text for analysis.";

    @Nested
    @DisplayName("Text Analysis Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create text analysis with language")
        void shouldCreateTextAnalysisWithLanguage() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            assertThat(analysis).isNotNull();
            assertThat(analysis.getText()).isEqualTo(SAMPLE_TEXT);
            assertThat(analysis.getDetectedLanguage()).isEqualTo(LanguageCode.EN);
            assertThat(analysis.getAnalysisId()).isNotNull();
            assertThat(analysis.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should create text analysis with AUTO language")
        void shouldCreateTextAnalysisWithAutoLanguage() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT);

            assertThat(analysis.getDetectedLanguage()).isNotNull();
        }

        @Test
        @DisplayName("Should reject null text")
        void shouldRejectNullText() {
            assertThatThrownBy(() -> TextAnalysis.create(null, LanguageCode.EN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject empty text")
        void shouldRejectEmptyText() {
            assertThatThrownBy(() -> TextAnalysis.create("   ", LanguageCode.EN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject text exceeding max length")
        void shouldRejectTextExceedingMaxLength() {
            String longText = "a".repeat(100001);

            assertThatThrownBy(() -> TextAnalysis.create(longText, LanguageCode.EN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("exceeds maximum");
        }
    }

    @Nested
    @DisplayName("Entity Management Tests")
    class EntityManagementTests {

        @Test
        @DisplayName("Should add entity")
        void shouldAddEntity() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            TextAnalysis.Entity entity = new TextAnalysis.Entity(
                    "John", EntityType.PERSON, 0, 4, 0.95
            );

            analysis.addEntity(entity);

            assertThat(analysis.getEntities()).hasSize(1);
            assertThat(analysis.getEntities().get(0)).isEqualTo(entity);
        }

        @Test
        @DisplayName("Should add multiple entities")
        void shouldAddMultipleEntities() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            List<TextAnalysis.Entity> entities = List.of(
                    new TextAnalysis.Entity("John", EntityType.PERSON, 0, 4, 0.95),
                    new TextAnalysis.Entity("Google", EntityType.ORGANIZATION, 14, 20, 0.98)
            );

            analysis.addEntities(entities);

            assertThat(analysis.getEntities()).hasSize(2);
        }

        @Test
        @DisplayName("Should filter entities by type")
        void shouldFilterEntitiesByType() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            analysis.addEntity(new TextAnalysis.Entity("John", EntityType.PERSON, 0, 4, 0.95));
            analysis.addEntity(new TextAnalysis.Entity("Google", EntityType.ORGANIZATION, 14, 20, 0.98));
            analysis.addEntity(new TextAnalysis.Entity("Paris", EntityType.LOCATION, 30, 35, 0.92));

            List<TextAnalysis.Entity> persons = analysis.getEntitiesByType(EntityType.PERSON);

            assertThat(persons).hasSize(1);
            assertThat(persons.get(0).type()).isEqualTo(EntityType.PERSON);
        }

        @Test
        @DisplayName("Should check if contains entity")
        void shouldCheckIfContainsEntity() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            analysis.addEntity(new TextAnalysis.Entity("Apple", EntityType.ORGANIZATION, 0, 5, 0.9));

            assertThat(analysis.containsEntity("Apple")).isTrue();
            assertThat(analysis.containsEntity("Microsoft")).isFalse();
        }

        @Test
        @DisplayName("Should ignore null entity")
        void shouldIgnoreNullEntity() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            int initialSize = analysis.getEntities().size();
            analysis.addEntity(null);

            assertThat(analysis.getEntities()).hasSize(initialSize);
        }
    }

    @Nested
    @DisplayName("Sentiment Analysis Tests")
    class SentimentTests {

        @Test
        @DisplayName("Should set sentiment")
        void shouldSetSentiment() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            TextAnalysis.SentimentResult sentiment = new TextAnalysis.SentimentResult(
                    SentimentLabel.POSITIVE, 0.75
            );

            analysis.setSentiment(sentiment);

            assertThat(analysis.getSentiment()).isNotNull();
            assertThat(analysis.getSentiment().label()).isEqualTo(SentimentLabel.POSITIVE);
            assertThat(analysis.getSentiment().score()).isEqualTo(0.75);
        }

        @Test
        @DisplayName("Should clamp sentiment score")
        void shouldClampSentimentScore() {
            TextAnalysis.SentimentResult sentiment = new TextAnalysis.SentimentResult(
                    SentimentLabel.POSITIVE, 2.5
            );

            assertThat(sentiment.score()).isEqualTo(1.0);
        }
    }

    @Nested
    @DisplayName("Categorization Tests")
    class CategorizationTests {

        @Test
        @DisplayName("Should set category score")
        void shouldSetCategoryScore() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            analysis.setCategoryScore("technology", 0.85);
            analysis.setCategoryScore("business", 0.65);

            assertThat(analysis.getCategoryScores()).hasSize(2);
            assertThat(analysis.getCategoryScores().get("technology")).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should clamp category score between 0 and 1")
        void shouldClampCategoryScore() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            analysis.setCategoryScore("test", 1.5);

            assertThat(analysis.getCategoryScores().get("test")).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should get top categories")
        void shouldGetTopCategories() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            analysis.setCategoryScore("technology", 0.9);
            analysis.setCategoryScore("business", 0.7);
            analysis.setCategoryScore("sports", 0.8);
            analysis.setCategoryScore("politics", 0.6);

            List<TextAnalysis.CategoryScore> top2 = analysis.getTopCategories(2);

            assertThat(top2).hasSize(2);
            assertThat(top2.get(0).category()).isEqualTo("technology");
            assertThat(top2.get(0).score()).isEqualTo(0.9);
        }
    }

    @Nested
    @DisplayName("Keyword Management Tests")
    class KeywordTests {

        @Test
        @DisplayName("Should add keyword")
        void shouldAddKeyword() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            TextAnalysis.Keyword keyword = new TextAnalysis.Keyword("machine learning", 0.92);

            analysis.addKeyword(keyword);

            assertThat(analysis.getKeywords()).hasSize(1);
            assertThat(analysis.getKeywords().get(0).text()).isEqualTo("machine learning");
        }

        @Test
        @DisplayName("Should add multiple keywords")
        void shouldAddMultipleKeywords() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            List<TextAnalysis.Keyword> keywords = List.of(
                    new TextAnalysis.Keyword("AI", 0.88),
                    new TextAnalysis.Keyword("NLP", 0.85)
            );

            analysis.addKeywords(keywords);

            assertThat(analysis.getKeywords()).hasSize(2);
        }

        @Test
        @DisplayName("Should ignore null keyword")
        void shouldIgnoreNullKeyword() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            int initialSize = analysis.getKeywords().size();
            analysis.addKeyword(null);

            assertThat(analysis.getKeywords()).hasSize(initialSize);
        }
    }

    @Nested
    @DisplayName("Summary Tests")
    class SummaryTests {

        @Test
        @DisplayName("Should set summary")
        void shouldSetSummary() {
            TextAnalysis analysis = TextAnalysis.create("Sample text for summary. ".repeat(20), LanguageCode.EN);

            String summary = "This is a summary of the text.";

            analysis.setSummary(summary);

            assertThat(analysis.getSummary()).isEqualTo(summary);
        }

        @Test
        @DisplayName("Should enforce minimum summary length")
        void shouldEnforceMinimumSummaryLength() {
            TextAnalysis analysis = TextAnalysis.create("This is a long text that needs a summary.", LanguageCode.EN);

            assertThatThrownBy(() -> analysis.setSummary("Short"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("at least");
        }

        @Test
        @DisplayName("Should accept summary at minimum length")
        void shouldAcceptSummaryAtMinLength() {
            TextAnalysis analysis = TextAnalysis.create("This is a long text that needs a summary.", LanguageCode.EN);

            int minSummaryLength = Math.max(10, "This is a long text that needs a summary.".length() / 10);

            assertThatCode(() -> analysis.setSummary("x".repeat(minSummaryLength)))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Analysis Properties Tests")
    class PropertiesTests {

        @Test
        @DisplayName("Should return text length")
        void shouldReturnTextLength() {
            String text = "Sample text";
            TextAnalysis analysis = TextAnalysis.create(text, LanguageCode.EN);

            assertThat(analysis.getTextLength()).isEqualTo(text.length());
        }

        @Test
        @DisplayName("Should return creation time")
        void shouldReturnCreationTime() {
            Instant before = Instant.now();

            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            Instant after = Instant.now();

            assertThat(analysis.getCreatedAt()).isNotNull();
            assertThat(analysis.getCreatedAt()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should have unique analysis ID")
        void shouldHaveUniqueAnalysisId() {
            TextAnalysis analysis1 = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);
            TextAnalysis analysis2 = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            assertThat(analysis1.getAnalysisId()).isNotNull();
            assertThat(analysis2.getAnalysisId()).isNotNull();
            assertThat(analysis1.getAnalysisId()).isNotEqualTo(analysis2.getAnalysisId());
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when same analysis ID")
        void shouldBeEqualWhenSameAnalysisId() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            assertThat(analysis).isEqualTo(analysis);
        }

        @Test
        @DisplayName("Should have consistent hash code")
        void shouldHaveConsistentHashCode() {
            TextAnalysis analysis = TextAnalysis.create(SAMPLE_TEXT, LanguageCode.EN);

            int hashCode1 = analysis.hashCode();
            int hashCode2 = analysis.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
