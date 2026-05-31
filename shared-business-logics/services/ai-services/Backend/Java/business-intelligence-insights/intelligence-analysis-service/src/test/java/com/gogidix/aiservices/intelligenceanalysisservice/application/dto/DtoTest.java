package com.gogidix.aiservices.intelligenceanalysisservice.application.dto;
import org.junit.jupiter.api.*; import java.util.Map; import static org.assertj.core.api.Assertions.*;
class DtoTest {
    @Test void analyzeReqNull() { assertThat(new AnalyzeAnalysisRequestDto(null).analysisOptions()).isEmpty(); }
    @Test void analyzeReqOpts() { assertThat(new AnalyzeAnalysisRequestDto(Map.of("k","v")).analysisOptions()).containsEntry("k","v"); }
    @Test void searchDefaults() { var d = new AnalysisSearchRequestDto(null,null,null,null,null,null); assertThat(d.page()).isEqualTo(0); assertThat(d.size()).isEqualTo(20); }
    @Test void errorResp() { var d = ErrorResponseDto.of(400,"E","m","/","c"); assertThat(d.status()).isEqualTo(400); assertThat(d.fieldErrors()).isNull(); }
    @Test void errorRespVal() { var d = ErrorResponseDto.validation("m","/","c",java.util.List.of(ErrorResponseDto.ValidationError.of("f","r",null))); assertThat(d.fieldErrors()).hasSize(1); }
}
