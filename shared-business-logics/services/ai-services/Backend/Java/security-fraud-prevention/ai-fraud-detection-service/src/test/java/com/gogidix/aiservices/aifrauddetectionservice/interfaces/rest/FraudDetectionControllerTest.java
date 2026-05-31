package com.gogidix.aiservices.aifrauddetectionservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aifrauddetectionservice.application.dto.request.AddPatternRequest;
import com.gogidix.aiservices.aifrauddetectionservice.application.dto.request.AnalyzeTransactionRequest;
import com.gogidix.aiservices.aifrauddetectionservice.application.dto.response.AnalysisResponse;
import com.gogidix.aiservices.aifrauddetectionservice.application.service.FraudDetectionService;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.gogidix.aiservices.aifrauddetectionservice.config.WebMvcTestConfig;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FraudDetectionController.class)
@Import(WebMvcTestConfig.class)
@DisplayName("FraudDetectionController REST API Tests")
class FraudDetectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FraudDetectionService fraudDetectionService;

    private static final String TRANSACTION_ID = "txn-123";
    private static final String USER_ID = "user-456";
    private static final String ANALYSIS_ID = "analysis-789";

    @Nested
    @DisplayName("POST /api/v1/fraud/analyze Tests")
    class AnalyzeEndpointTests {

        @Test
        @DisplayName("Should analyze transaction successfully and return 200")
        void shouldAnalyzeTransactionSuccessfully() throws Exception {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.2)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            when(fraudDetectionService.analyzeTransaction(any())).thenReturn(result);

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analysisId").value(ANALYSIS_ID))
                    .andExpect(jsonPath("$.transactionId").value(TRANSACTION_ID))
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.fraudScore").value(0.2))
                    .andExpect(jsonPath("$.riskLevel").value("LOW"))
                    .andExpect(jsonPath("$.action").value("ALLOW"));

            verify(fraudDetectionService).analyzeTransaction(any());
        }

        @Test
        @DisplayName("Should return 400 when transactionId is missing")
        void shouldReturn400WhenTransactionIdMissing() throws Exception {
            String requestJson = """
                    {
                        "userId": "user-456",
                        "amount": 100,
                        "merchant": "Test Merchant",
                        "currency": "USD"
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).analyzeTransaction(any());
        }

        @Test
        @DisplayName("Should return 400 when userId is missing")
        void shouldReturn400WhenUserIdMissing() throws Exception {
            String requestJson = """
                    {
                        "transactionId": "txn-123",
                        "amount": 100,
                        "merchant": "Test Merchant",
                        "currency": "USD"
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).analyzeTransaction(any());
        }

        @Test
        @DisplayName("Should return 400 when amount is missing")
        void shouldReturn400WhenAmountMissing() throws Exception {
            String requestJson = """
                    {
                        "transactionId": "txn-123",
                        "userId": "user-456",
                        "merchant": "Test Merchant",
                        "currency": "USD"
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).analyzeTransaction(any());
        }

        @Test
        @DisplayName("Should return 400 when amount is negative")
        void shouldReturn400WhenAmountIsNegative() throws Exception {
            String requestJson = """
                    {
                        "transactionId": "txn-123",
                        "userId": "user-456",
                        "amount": -100,
                        "merchant": "Test Merchant",
                        "currency": "USD"
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).analyzeTransaction(any());
        }

        @Test
        @DisplayName("Should return 400 when amount is zero")
        void shouldReturn400WhenAmountIsZero() throws Exception {
            String requestJson = """
                    {
                        "transactionId": "txn-123",
                        "userId": "user-456",
                        "amount": 0,
                        "merchant": "Test Merchant",
                        "currency": "USD"
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).analyzeTransaction(any());
        }

        @Test
        @DisplayName("Should return 400 when merchant is missing")
        void shouldReturn400WhenMerchantMissing() throws Exception {
            String requestJson = """
                    {
                        "transactionId": "txn-123",
                        "userId": "user-456",
                        "amount": 100,
                        "currency": "USD"
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).analyzeTransaction(any());
        }

        @Test
        @DisplayName("Should return 400 for malformed JSON")
        void shouldReturn400ForMalformedJson() throws Exception {
            String malformedJson = """
                    {
                        "transactionId": "txn-123",
                        "userId": "user-456",
                        "amount": 100,
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(malformedJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).analyzeTransaction(any());
        }

        @Test
        @DisplayName("Should analyze transaction with metadata")
        void shouldAnalyzeTransactionWithMetadata() throws Exception {
            String requestJson = """
                    {
                        "transactionId": "txn-123",
                        "userId": "user-456",
                        "amount": 100,
                        "merchant": "Test Merchant",
                        "currency": "USD",
                        "metadata": {
                            "new_device": true,
                            "ip_address": "192.168.1.1"
                        }
                    }
                    """;

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.3)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            when(fraudDetectionService.analyzeTransaction(any())).thenReturn(result);

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analysisId").exists());

            verify(fraudDetectionService).analyzeTransaction(any());
        }

        @Test
        @DisplayName("Should return high risk analysis result")
        void shouldReturnHighRiskAnalysisResult() throws Exception {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("15000"))
                    .merchant("High Risk Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("High fraud probability", "High-value transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            when(fraudDetectionService.analyzeTransaction(any())).thenReturn(result);

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.fraudScore").value(0.9))
                    .andExpect(jsonPath("$.riskLevel").value("HIGH"))
                    .andExpect(jsonPath("$.action").value("BLOCK"))
                    .andExpect(jsonPath("$.reasons").isArray())
                    .andExpect(jsonPath("$.reasons[0]").value("High fraud probability"));

            verify(fraudDetectionService).analyzeTransaction(any());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/fraud/patterns Tests")
    class GetPatternsTests {

        @Test
        @DisplayName("Should return all fraud patterns")
        void shouldReturnAllFraudPatterns() throws Exception {
            List<FraudPattern> patterns = List.of(
                    FraudPattern.builder()
                            .patternId("pattern-1")
                            .patternName("Velocity Check")
                            .description("Multiple transactions in short time")
                            .confidenceScore(0.9)
                            .lastSeen(Instant.now())
                            .occurrenceCount(10)
                            .build(),
                    FraudPattern.builder()
                            .patternId("pattern-2")
                            .patternName("Location Anomaly")
                            .description("Unusual location")
                            .confidenceScore(0.85)
                            .lastSeen(Instant.now())
                            .occurrenceCount(5)
                            .build()
            );

            when(fraudDetectionService.getFraudPatterns()).thenReturn(patterns);

            mockMvc.perform(get("/api/v1/fraud/patterns"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].patternId").value("pattern-1"))
                    .andExpect(jsonPath("$[0].patternName").value("Velocity Check"))
                    .andExpect(jsonPath("$[1].patternId").value("pattern-2"))
                    .andExpect(jsonPath("$[1].patternName").value("Location Anomaly"));

            verify(fraudDetectionService).getFraudPatterns();
        }

        @Test
        @DisplayName("Should return empty list when no patterns exist")
        void shouldReturnEmptyListWhenNoPatterns() throws Exception {
            when(fraudDetectionService.getFraudPatterns()).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/fraud/patterns"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());

            verify(fraudDetectionService).getFraudPatterns();
        }
    }

    @Nested
    @DisplayName("POST /api/v1/fraud/patterns Tests")
    class AddPatternTests {

        @Test
        @DisplayName("Should add new pattern and return 202")
        void shouldAddNewPattern() throws Exception {
            AddPatternRequest request = new AddPatternRequest();
            request.setPatternName("New Pattern");
            request.setDescription("A new fraud pattern");
            request.setConfidenceScore(0.75);

            doNothing().when(fraudDetectionService).addFraudPattern(any(FraudPattern.class));

            mockMvc.perform(post("/api/v1/fraud/patterns")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());

            verify(fraudDetectionService).addFraudPattern(any(FraudPattern.class));
        }

        @Test
        @DisplayName("Should return 400 when patternName is missing")
        void shouldReturn400WhenPatternNameMissing() throws Exception {
            String requestJson = """
                    {
                        "description": "A new fraud pattern",
                        "confidenceScore": 0.75
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/patterns")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).addFraudPattern(any());
        }

        @Test
        @DisplayName("Should return 400 when description is missing")
        void shouldReturn400WhenDescriptionMissing() throws Exception {
            String requestJson = """
                    {
                        "patternName": "New Pattern",
                        "confidenceScore": 0.75
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/patterns")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).addFraudPattern(any());
        }

        @Test
        @DisplayName("Should return 400 when confidenceScore is below 0.0")
        void shouldReturn400WhenConfidenceScoreBelowZero() throws Exception {
            String requestJson = """
                    {
                        "patternName": "New Pattern",
                        "description": "A new fraud pattern",
                        "confidenceScore": -0.1
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/patterns")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).addFraudPattern(any());
        }

        @Test
        @DisplayName("Should return 400 when confidenceScore is above 1.0")
        void shouldReturn400WhenConfidenceScoreAboveOne() throws Exception {
            String requestJson = """
                    {
                        "patternName": "New Pattern",
                        "description": "A new fraud pattern",
                        "confidenceScore": 1.1
                    }
                    """;

            mockMvc.perform(post("/api/v1/fraud/patterns")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());

            verify(fraudDetectionService, never()).addFraudPattern(any());
        }

        @Test
        @DisplayName("Should accept confidenceScore of 0.0")
        void shouldAcceptConfidenceScoreOfZero() throws Exception {
            String requestJson = """
                    {
                        "patternName": "New Pattern",
                        "description": "A new fraud pattern",
                        "confidenceScore": 0.0
                    }
                    """;

            doNothing().when(fraudDetectionService).addFraudPattern(any(FraudPattern.class));

            mockMvc.perform(post("/api/v1/fraud/patterns")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isAccepted());

            verify(fraudDetectionService).addFraudPattern(any(FraudPattern.class));
        }

        @Test
        @DisplayName("Should accept confidenceScore of 1.0")
        void shouldAcceptConfidenceScoreOfOne() throws Exception {
            String requestJson = """
                    {
                        "patternName": "New Pattern",
                        "description": "A new fraud pattern",
                        "confidenceScore": 1.0
                    }
                    """;

            doNothing().when(fraudDetectionService).addFraudPattern(any(FraudPattern.class));

            mockMvc.perform(post("/api/v1/fraud/patterns")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isAccepted());

            verify(fraudDetectionService).addFraudPattern(any(FraudPattern.class));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/fraud/analysis/{analysisId} Tests")
    class GetAnalysisTests {

        @Test
        @DisplayName("Should return analysis result by ID")
        void shouldReturnAnalysisById() throws Exception {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.3)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            when(fraudDetectionService.getAnalysisResult(ANALYSIS_ID)).thenReturn(result);

            mockMvc.perform(get("/api/v1/fraud/analysis/{analysisId}", ANALYSIS_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analysisId").value(ANALYSIS_ID))
                    .andExpect(jsonPath("$.transactionId").value(TRANSACTION_ID))
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.fraudScore").value(0.3));

            verify(fraudDetectionService).getAnalysisResult(ANALYSIS_ID);
        }

        @Test
        @DisplayName("Should return 400 when analysis not found")
        void shouldReturn404WhenAnalysisNotFound() throws Exception {
            String nonExistentId = "non-existent";

            when(fraudDetectionService.getAnalysisResult(nonExistentId))
                    .thenThrow(new IllegalArgumentException("Analysis not found: " + nonExistentId));

            mockMvc.perform(get("/api/v1/fraud/analysis/{analysisId}", nonExistentId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());

            verify(fraudDetectionService).getAnalysisResult(nonExistentId);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/fraud/history/{userId} Tests")
    class GetHistoryTests {

        @Test
        @DisplayName("Should return user analysis history with default limit")
        void shouldReturnUserHistoryWithDefaultLimit() throws Exception {
            List<FraudAnalysisResult> history = List.of(
                    FraudAnalysisResult.builder()
                            .analysisId("analysis-1")
                            .transactionId("txn-1")
                            .userId(USER_ID)
                            .fraudScore(0.2)
                            .riskLevel(RiskLevel.LOW)
                            .recommendedAction(FraudAction.ALLOW)
                            .reasons(List.of("Normal"))
                            .timestamp(Instant.now())
                            .modelVersion("1.0.0")
                            .build()
            );

            when(fraudDetectionService.getUserAnalysisHistory(eq(USER_ID), anyInt())).thenReturn(history);

            mockMvc.perform(get("/api/v1/fraud/history/{userId}", USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].userId").value(USER_ID));

            verify(fraudDetectionService).getUserAnalysisHistory(eq(USER_ID), eq(10));
        }

        @Test
        @DisplayName("Should return user analysis history with custom limit")
        void shouldReturnUserHistoryWithCustomLimit() throws Exception {
            int customLimit = 25;

            when(fraudDetectionService.getUserAnalysisHistory(eq(USER_ID), eq(customLimit)))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/fraud/history/{userId}", USER_ID)
                            .param("limit", String.valueOf(customLimit)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());

            verify(fraudDetectionService).getUserAnalysisHistory(eq(USER_ID), eq(customLimit));
        }

        @Test
        @DisplayName("Should return empty list for user with no history")
        void shouldReturnEmptyListForNoHistory() throws Exception {
            when(fraudDetectionService.getUserAnalysisHistory(eq(USER_ID), anyInt())).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/fraud/history/{userId}", USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());

            verify(fraudDetectionService).getUserAnalysisHistory(eq(USER_ID), eq(10));
        }

        @Test
        @DisplayName("Should return multiple analysis results")
        void shouldReturnMultipleAnalysisResults() throws Exception {
            List<FraudAnalysisResult> history = List.of(
                    FraudAnalysisResult.builder()
                            .analysisId("analysis-1")
                            .transactionId("txn-1")
                            .userId(USER_ID)
                            .fraudScore(0.2)
                            .riskLevel(RiskLevel.LOW)
                            .recommendedAction(FraudAction.ALLOW)
                            .reasons(List.of("Normal"))
                            .timestamp(Instant.now())
                            .modelVersion("1.0.0")
                            .build(),
                    FraudAnalysisResult.builder()
                            .analysisId("analysis-2")
                            .transactionId("txn-2")
                            .userId(USER_ID)
                            .fraudScore(0.6)
                            .riskLevel(RiskLevel.MEDIUM)
                            .recommendedAction(FraudAction.REVIEW)
                            .reasons(List.of("Suspicious"))
                            .timestamp(Instant.now())
                            .modelVersion("1.0.0")
                            .build()
            );

            when(fraudDetectionService.getUserAnalysisHistory(eq(USER_ID), anyInt())).thenReturn(history);

            mockMvc.perform(get("/api/v1/fraud/history/{userId}", USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].analysisId").value("analysis-1"))
                    .andExpect(jsonPath("$[1].analysisId").value("analysis-2"));

            verify(fraudDetectionService).getUserAnalysisHistory(eq(USER_ID), eq(10));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle IllegalArgumentException with error response")
        void shouldHandleIllegalArgumentException() throws Exception {
            when(fraudDetectionService.getAnalysisResult("invalid-id"))
                    .thenThrow(new IllegalArgumentException("Invalid analysis ID"));

            mockMvc.perform(get("/api/v1/fraud/analysis/invalid-id"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").exists());

            verify(fraudDetectionService).getAnalysisResult("invalid-id");
        }

        @Test
        @DisplayName("Should return 404 for non-existent endpoint")
        void shouldReturn404ForNonExistentEndpoint() throws Exception {
            mockMvc.perform(get("/api/v1/fraud/non-existent"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 415 for unsupported media type")
        void shouldReturn415ForUnsupportedMediaType() throws Exception {
            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());
        }
    }

    @Nested
    @DisplayName("Content Type Tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should accept application/json content type")
        void shouldAcceptApplicationJson() throws Exception {
            String requestJson = """
                    {
                        "transactionId": "txn-123",
                        "userId": "user-456",
                        "amount": 100,
                        "merchant": "Test Merchant",
                        "currency": "USD"
                    }
                    """;

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.2)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            when(fraudDetectionService.analyzeTransaction(any())).thenReturn(result);

            mockMvc.perform(post("/api/v1/fraud/analyze")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return application/json content type")
        void shouldReturnApplicationJson() throws Exception {
            when(fraudDetectionService.getFraudPatterns()).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/fraud/patterns"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }
}
