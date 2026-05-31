package com.gogidix.aiservices.performanceoptimizationservice.application.service;
import com.gogidix.aiservices.performanceoptimizationservice.domain.model.PerformanceAnalysis;
import com.gogidix.aiservices.performanceoptimizationservice.domain.repository.PerformanceAnalysisRepository;
import com.gogidix.aiservices.performanceoptimizationservice.shared.exception.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PerformanceAnalysisService {
    private final PerformanceAnalysisRepository repository;
    public PerformanceAnalysisService(PerformanceAnalysisRepository repository) { this.repository = repository; }
    public PerformanceAnalysis analyze(String tenantId, String service, List<String> metrics) {
        PerformanceAnalysis analysis = new PerformanceAnalysis(tenantId, service, metrics);
        return repository.save(analysis);
    }
    public PerformanceAnalysis getAnalysisById(String analysisId, String tenantId) {
        return repository.findByAnalysisIdAndTenantId(analysisId, tenantId)
                .orElseThrow(() -> new NotFoundException("PerformanceAnalysis", analysisId));
    }
    public void clearCache(String pattern) { /* Cache clearing logic */ }
    public List<PerformanceAnalysis> getAnalysesByTenant(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
