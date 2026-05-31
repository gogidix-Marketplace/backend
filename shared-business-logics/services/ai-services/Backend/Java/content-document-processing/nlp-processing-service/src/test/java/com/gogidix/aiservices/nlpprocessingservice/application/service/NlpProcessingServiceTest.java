package com.gogidix.aiservices.nlpprocessingservice.application.service;
import org.junit.jupiter.api.*;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.request.*;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.response.*;
import com.gogidix.aiservices.nlpprocessingservice.domain.aggregate.TextAnalysis;
import com.gogidix.aiservices.nlpprocessingservice.domain.model.*;
import com.gogidix.aiservices.nlpprocessingservice.domain.port.out.NlpEnginePort;
import com.gogidix.aiservices.nlpprocessingservice.shared.exception.NlpProcessingException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Map;

class NlpProcessingServiceTest {
    private NlpEnginePort engine;
    private NlpProcessingService svc;

    @BeforeEach
    void setup() {
        engine = mock(NlpEnginePort.class);
        svc = new NlpProcessingService(engine);
    }

    @Test
    void analyzeTextAllFeatures() {
        when(engine.detectLanguage(anyString())).thenReturn(LanguageCode.EN);
        when(engine.extractEntities(anyString(), any())).thenReturn(List.of(
            new TextAnalysis.Entity("IBM", EntityType.ORGANIZATION, 0, 3, 0.95)));
        when(engine.analyzeSentiment(anyString(), any())).thenReturn(
            new TextAnalysis.SentimentResult(SentimentLabel.POSITIVE, 0.8));
        when(engine.categorizeText(anyString(), any())).thenReturn(Map.of("tech", 0.9));
        when(engine.extractKeywords(anyString(), any())).thenReturn(List.of(
            new TextAnalysis.Keyword("AI", 0.7)));

        var req = new AnalyzeTextRequest("IBM is great", LanguageCode.AUTO,
            AnalyzeTextRequest.FeatureConfig.builder().entities(true).sentiment(true).categories(true).keywords(true).build());
        var r = svc.analyzeText(req);
        assertThat(r).isNotNull();
        assertThat(r.language()).isEqualTo("EN");
    }

    @Test
    void analyzeTextTooLong() {
        var big = "x".repeat(100001);
        var req = new AnalyzeTextRequest(big, LanguageCode.EN,
            AnalyzeTextRequest.FeatureConfig.builder().entities(true).build());
        assertThatThrownBy(() -> svc.analyzeText(req)).isInstanceOf(NlpProcessingException.class);
    }

    @Test
    void summarizeText() {
        when(engine.summarize(anyString(), any(), anyDouble())).thenReturn("short");
        var req = new SummarizeTextRequest("Long text here", LanguageCode.EN, 0.3);
        var r = svc.summarizeText(req);
        assertThat(r).isNotNull();
        assertThat(r.summary()).isEqualTo("short");
    }
}
