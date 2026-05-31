package com.gogidix.aiservices.aifrauddetectionservice.domain.port.in;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Input port for fraud detection operations.
 * This use case defines the contract for analyzing transactions and managing
 * fraud detection patterns within the system.
 *
 * Following hexagonal architecture principles, this interface represents
 * an input port that will be implemented by application services.
 */
@UseCase(
    value = "Handles fraud detection analysis and pattern management",
    category = "command"
)
public interface FraudDetectionUseCase {

    /**
     * Analyzes a transaction for potential fraud using AI-powered detection.
     *
     * @param transactionId Unique identifier of the transaction to analyze
     * @param tenantId Tenant identifier for multi-tenancy support
     * @param transactionData Raw transaction data for analysis
     * @return CompletableFuture containing the analysis result ID
     */
    CompletableFuture<UUID> analyzeTransaction(UUID transactionId, String tenantId, Object transactionData);

    /**
     * Adds a new fraud pattern to the detection system.
     * This pattern will be used in future transaction analyses.
     *
     * @param patternType Type of fraud pattern (e.g., "VELOCITY", "AMOUNT", "LOCATION")
     * @param patternDefinition Pattern definition in JSON or rule format
     * @param threshold Risk threshold associated with this pattern
     * @param description Human-readable description of the pattern
     * @param addedBy User or system identifier that added this pattern
     * @return UUID of the newly created pattern
     */
    UUID addFraudPattern(String patternType, String patternDefinition, Double threshold,
                         String description, String addedBy);

    /**
     * Retrieves the result of a previously initiated fraud analysis.
     *
     * @param analysisId Unique identifier of the analysis
     * @return FraudAnalysisResult containing the analysis outcome and risk score
     */
    FraudAnalysisResult getAnalysisResult(UUID analysisId);

    /**
     * Value object representing the result of a fraud analysis.
     */
    record FraudAnalysisResult(
        UUID analysisId,
        UUID transactionId,
        String result, // "FRAUD_DETECTED", "CLEAN", "SUSPICIOUS", "REQUIRES_REVIEW"
        Double riskScore,
        String reasoning,
        java.time.Instant timestamp
    ) {}
}
