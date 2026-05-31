package com.gogidix.aiservices.aifrauddetectionservice.domain.port.out;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;

import java.util.List;
import java.util.Optional;

public interface FraudRepository {
    void saveAnalysisResult(FraudAnalysisResult result);
    Optional<FraudAnalysisResult> findAnalysisById(String analysisId);
    List<FraudAnalysisResult> findByUserId(String userId, int limit);
    void savePattern(FraudPattern pattern);
    List<FraudPattern> getActivePatterns();
}
