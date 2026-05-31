package com.gogidix.aiservices.nlpprocessingservice.application.dto;
import org.junit.jupiter.api.*;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.request.*;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.response.*;
import com.gogidix.aiservices.nlpprocessingservice.domain.model.LanguageCode;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class DtoTest {
    @Test void analyzeRequest() {
        var r = new AnalyzeTextRequest("hello", LanguageCode.EN, AnalyzeTextRequest.FeatureConfig.builder().entities(true).sentiment(true).build());
        assertThat(r.text()).isEqualTo("hello");
        assertThat(r.features().entities()).isTrue();
    }
    @Test void analyzeRequestDefaults() {
        var r = new AnalyzeTextRequest("hello", null, null);
        assertThat(r.language()).isNull();
    }
    @Test void featureConfigDefaults() {
        var f = AnalyzeTextRequest.FeatureConfig.builder().build();
        assertThat(f.entities()).isTrue();
    }
    @Test void summarizeRequest() {
        var r = new SummarizeTextRequest("text", LanguageCode.EN, 0.5);
        assertThat(r.text()).isEqualTo("text");
        assertThat(r.ratio()).isEqualTo(0.5);
    }
    @Test void summarizeDefaults() {
        var r = new SummarizeTextRequest("text");
        assertThat(r.language()).isEqualTo(LanguageCode.AUTO);
        assertThat(r.ratio()).isEqualTo(0.3);
    }
    @Test void summarizeNullLang() {
        var r = new SummarizeTextRequest("text", null);
        assertThat(r.language()).isEqualTo(LanguageCode.AUTO);
    }
    @Test void analysisResponse() {
        var r = new TextAnalysisResponse("id", "en", List.of(), null, List.of(), List.of());
        assertThat(r.analysisId()).isEqualTo("id");
    }
    @Test void summaryResponse() {
        var r = new TextSummaryResponse("sum", 100, 30, LanguageCode.EN);
        assertThat(r.summary()).isEqualTo("sum");
    }
    @Test void entityDto() { var d = new TextAnalysisResponse.EntityDto("t","PER",0.9); assertThat(d.text()).isEqualTo("t"); }
    @Test void sentimentDto() { var d = new TextAnalysisResponse.SentimentDto("POS",0.8); assertThat(d.label()).isEqualTo("POS"); }
    @Test void categoryDto() { var d = new TextAnalysisResponse.CategoryDto("tech",0.7); assertThat(d.label()).isEqualTo("tech"); }
    @Test void keywordDto() { var d = new TextAnalysisResponse.KeywordDto("ai",0.6); assertThat(d.text()).isEqualTo("ai"); }
}
