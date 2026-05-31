package com.gogidix.aiservices.aifrauddetectionservice.application.service;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.*;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.FraudRepository;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.MlModelPort;
import com.gogidix.aiservices.aifrauddetectionservice.domain.policy.FraudDetectionPolicy;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.metrics.FraudDetectionMetrics;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final FraudRepository fraudRepository;
    private final MlModelPort mlModelPort;
    private final FraudDetectionPolicy policy;
    private final FraudDetectionMetrics metrics;

    public FraudAnalysisResult analyzeTransaction(Transaction transaction) {
        metrics.incrementAnalysisTotal();

        Timer.Sample analysisTimer = metrics.startAnalysisTimer();

        try {
            // Validate transaction
            policy.validateTransaction(transaction);

            // Get ML model prediction
            Timer.Sample mlTimer = metrics.startMlPredictionTimer();
            double rawScore = mlModelPort.predictFraudScore(transaction);
            metrics.stopMlPredictionTimer(mlTimer);

            // Apply business rules
            double adjustedScore = policy.adjustScore(transaction, rawScore);

            // Determine risk level and action
            RiskLevel riskLevel = policy.classifyRisk(adjustedScore);
            FraudAction action = policy.determineAction(adjustedScore);

            // Get reasons
            List<String> reasons = policy.getFraudReasons(transaction, adjustedScore);

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(UUID.randomUUID().toString())
                    .transactionId(transaction.getTransactionId())
                    .userId(transaction.getUserId())
                    .tenantId(transaction.getTenantId())
                    .fraudScore(adjustedScore)
                    .riskLevel(riskLevel)
                    .recommendedAction(action)
                    .reasons(reasons)
                    .timestamp(Instant.now())
                    .modelVersion(mlModelPort.getModelVersion())
                    .build();

            // Save result with metrics
            Timer.Sample dbTimer = metrics.startDatabaseSaveTimer();
            fraudRepository.saveAnalysisResult(result);
            metrics.stopDatabaseSaveTimer(dbTimer);

            // Record success and risk metrics
            metrics.incrementAnalysisSuccess();

            if (riskLevel == RiskLevel.HIGH) {
                metrics.incrementHighRisk();
            }
            if (action == FraudAction.BLOCK) {
                metrics.incrementBlocked();
            }
            if (adjustedScore > 0.7) {
                metrics.incrementFraudDetected();
            }

            return result;

        } catch (Exception e) {
            metrics.incrementAnalysisFailure();
            throw e;
        } finally {
            metrics.stopAnalysisTimer(analysisTimer);
        }
    }

    public List<FraudPattern> getFraudPatterns() {
        return fraudRepository.getActivePatterns();
    }

    public void addFraudPattern(FraudPattern pattern) {
        fraudRepository.savePattern(pattern);
    }

    public void saveAnalysisResult(FraudAnalysisResult result) {
        fraudRepository.saveAnalysisResult(result);
    }

    public FraudAnalysisResult getAnalysisResult(String analysisId) {
        return fraudRepository.findAnalysisById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("Analysis not found: " + analysisId));
    }

    public java.util.Optional<FraudAnalysisResult> findAnalysisById(String analysisId) {
        return fraudRepository.findAnalysisById(analysisId);
    }

    public List<FraudAnalysisResult> getUserAnalysisHistory(String userId, int limit) {
        return fraudRepository.findByUserId(userId, limit);
    }
}
