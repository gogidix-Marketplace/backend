package com.gogidix.aiservices.aifrauddetectionservice.domain.policy;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FraudDetectionPolicy {
    private static final double BLOCK_THRESHOLD = 0.8;
    private static final double REVIEW_THRESHOLD = 0.5;
    private static final BigDecimal HIGH_AMOUNT_THRESHOLD = new BigDecimal("10000");

    public void validateTransaction(Transaction transaction) {
        if (transaction.getTransactionId() == null || transaction.getTransactionId().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID is required");
        }
        if (transaction.getUserId() == null || transaction.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        // Allow zero amounts for edge cases like authorization holds, but flag them in scoring
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative");
        }
        if (transaction.getMerchant() == null || transaction.getMerchant().isEmpty()) {
            throw new IllegalArgumentException("Merchant is required");
        }
    }

    public double adjustScore(Transaction transaction, double rawScore) {
        double adjustedScore = rawScore;

        // Zero amounts are unusual - increase fraud score
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            adjustedScore += 0.3;
        }

        // Increase score for high-value transactions
        if (transaction.getAmount().compareTo(HIGH_AMOUNT_THRESHOLD) > 0) {
            adjustedScore += 0.1;
        }

        // Cap at 1.0
        return Math.min(adjustedScore, 1.0);
    }

    public RiskLevel classifyRisk(double score) {
        if (score <= 0.3) return RiskLevel.LOW;
        if (score <= 0.7) return RiskLevel.MEDIUM;
        return RiskLevel.HIGH;
    }

    public FraudAction determineAction(double score) {
        if (score > BLOCK_THRESHOLD) return FraudAction.BLOCK;
        if (score > REVIEW_THRESHOLD) return FraudAction.REVIEW;
        return FraudAction.ALLOW;
    }

    public List<String> getFraudReasons(Transaction transaction, double score) {
        List<String> reasons = new ArrayList<>();

        if (score > 0.8) {
            reasons.add("High fraud probability detected");
        }
        if (transaction.getAmount().compareTo(HIGH_AMOUNT_THRESHOLD) > 0) {
            reasons.add("High-value transaction");
        }
        if (score > 0.5 && score <= 0.8) {
            reasons.add("Suspicious activity pattern");
        }

        if (reasons.isEmpty()) {
            reasons.add("Normal transaction pattern");
        }

        return reasons;
    }
}
