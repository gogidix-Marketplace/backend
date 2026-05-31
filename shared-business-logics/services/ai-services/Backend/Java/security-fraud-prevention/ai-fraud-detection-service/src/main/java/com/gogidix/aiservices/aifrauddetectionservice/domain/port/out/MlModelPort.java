package com.gogidix.aiservices.aifrauddetectionservice.domain.port.out;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.Transaction;

public interface MlModelPort {
    double predictFraudScore(Transaction transaction);
    String getModelVersion();
}
