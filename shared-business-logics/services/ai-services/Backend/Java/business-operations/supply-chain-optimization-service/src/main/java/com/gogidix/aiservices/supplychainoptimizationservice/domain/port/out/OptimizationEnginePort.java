package com.gogidix.aiservices.supplychainoptimizationservice.domain.port.out;

import com.gogidix.aiservices.supplychainoptimizationservice.domain.aggregate.OptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationResult;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationType;

import java.util.List;
import java.util.Map;

public interface OptimizationEnginePort {
    OptimizationResult generateOptimization(OptimizationRequest request);
    List<OptimizationResult> generateBatchOptimizations(List<OptimizationRequest> requests);
    Map<String, Object> analyzeSupplyChainData(String tenantId, OptimizationType type);
    boolean validateParameters(OptimizationType type, Map<String, Object> parameters);
    double estimateProcessingTime(OptimizationType type, Map<String, Object> parameters);
}
