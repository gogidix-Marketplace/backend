package com.gogidix.aiservices.performanceoptimizationservice.domain.repository;
import com.gogidix.aiservices.performanceoptimizationservice.domain.model.PerformanceAnalysis;
import java.util.List;
import java.util.Optional;
public interface PerformanceAnalysisRepository {
    PerformanceAnalysis save(PerformanceAnalysis analysis);
    Optional<PerformanceAnalysis> findById(String id);
    Optional<PerformanceAnalysis> findByAnalysisIdAndTenantId(String analysisId, String tenantId);
    List<PerformanceAnalysis> findByTenantId(String tenantId);
    void deleteById(String id);
}
