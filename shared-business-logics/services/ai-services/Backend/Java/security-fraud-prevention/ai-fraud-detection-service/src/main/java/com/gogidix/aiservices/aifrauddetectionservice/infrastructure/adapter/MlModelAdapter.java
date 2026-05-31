package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.adapter;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.Transaction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.MlModelPort;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Map;

@Component
public class MlModelAdapter implements MlModelPort {
    private static final String MODEL_VERSION = "1.0.0";
    private final SecureRandom random = new SecureRandom();

    @Override
    public double predictFraudScore(Transaction transaction) {
        // Simulated ML prediction
        // In production, this would call an actual ML service
        double baseScore = random.nextDouble() * 0.5;

        // Increase score based on amount
        if (transaction.getAmount().compareTo(new java.math.BigDecimal("5000")) > 0) {
            baseScore += 0.2;
        }
        if (transaction.getAmount().compareTo(new java.math.BigDecimal("10000")) > 0) {
            baseScore += 0.2;
        }

        // Check metadata for risk factors
        if (transaction.getMetadata() != null) {
            if (transaction.getMetadata().containsKey("new_device")) {
                baseScore += 0.15;
            }
            if (transaction.getMetadata().containsKey("unusual_location")) {
                baseScore += 0.1;
            }
        }

        return Math.min(baseScore, 1.0);
    }

    @Override
    public String getModelVersion() {
        return MODEL_VERSION;
    }
}
