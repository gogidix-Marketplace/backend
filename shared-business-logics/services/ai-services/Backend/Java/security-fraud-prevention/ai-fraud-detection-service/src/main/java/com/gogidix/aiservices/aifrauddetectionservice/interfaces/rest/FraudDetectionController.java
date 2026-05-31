package com.gogidix.aiservices.aifrauddetectionservice.interfaces.rest;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.request.AnalyzeTransactionRequest;
import com.gogidix.aiservices.aifrauddetectionservice.application.dto.request.AddPatternRequest;
import com.gogidix.aiservices.aifrauddetectionservice.application.dto.response.AnalysisResponse;
import com.gogidix.aiservices.aifrauddetectionservice.application.service.FraudDetectionService;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.Transaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fraud")
@RequiredArgsConstructor
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyze(@Valid @RequestBody AnalyzeTransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .transactionId(request.getTransactionId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .merchant(request.getMerchant())
                .timestamp(request.getTimestamp())
                .currency(request.getCurrency())
                .metadata(request.getMetadata())
                .build();

        FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

        return ResponseEntity.ok(AnalysisResponse.fromDomain(result));
    }

    @GetMapping("/patterns")
    public ResponseEntity<List<FraudPattern>> getPatterns() {
        return ResponseEntity.ok(fraudDetectionService.getFraudPatterns());
    }

    @PostMapping("/patterns")
    public ResponseEntity<Void> addPattern(@Valid @RequestBody AddPatternRequest request) {
        FraudPattern pattern = FraudPattern.builder()
                .patternId(java.util.UUID.randomUUID().toString())
                .patternName(request.getPatternName())
                .description(request.getDescription())
                .confidenceScore(request.getConfidenceScore())
                .lastSeen(java.time.Instant.now())
                .occurrenceCount(1)
                .build();

        fraudDetectionService.addFraudPattern(pattern);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<FraudAnalysisResult> getAnalysis(@PathVariable String analysisId) {
        return ResponseEntity.ok(fraudDetectionService.getAnalysisResult(analysisId));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<FraudAnalysisResult>> getHistory(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(fraudDetectionService.getUserAnalysisHistory(userId, limit));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    record ErrorResponse(String error, String message) {
        public ErrorResponse(String message) {
            this("error", message);
        }
    }
}
