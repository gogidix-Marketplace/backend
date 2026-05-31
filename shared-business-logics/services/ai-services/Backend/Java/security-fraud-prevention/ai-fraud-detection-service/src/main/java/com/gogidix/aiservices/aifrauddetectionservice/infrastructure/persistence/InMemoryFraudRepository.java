package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.FraudRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryFraudRepository implements FraudRepository {
    private final Map<String, FraudAnalysisResult> analyses = new ConcurrentHashMap<>();
    private final Map<String, FraudPattern> patterns = new ConcurrentHashMap<>();
    private final Map<String, List<String>> userAnalyses = new ConcurrentHashMap<>();

    private static final int MAX_RETENTION_DAYS = 180;

    @Override
    public void saveAnalysisResult(FraudAnalysisResult result) {
        analyses.put(result.getAnalysisId(), result);
        // Atomic compute to prevent race condition with concurrent saves
        userAnalyses.compute(result.getUserId(), (k, v) -> {
            if (v == null) {
                v = new ArrayList<>();
            }
            v.add(result.getAnalysisId());
            return v;
        });

        // Cleanup old patterns
        cleanupOldData();
    }

    @Override
    public java.util.Optional<FraudAnalysisResult> findAnalysisById(String analysisId) {
        return java.util.Optional.ofNullable(analyses.get(analysisId));
    }

    @Override
    public List<FraudAnalysisResult> findByUserId(String userId, int limit) {
        return userAnalyses.getOrDefault(userId, List.of()).stream()
                .limit(limit)
                .map(analyses::get)
                .collect(Collectors.toList());
    }

    @Override
    public void savePattern(FraudPattern pattern) {
        patterns.put(pattern.getPatternId(), pattern);
    }

    @Override
    public List<FraudPattern> getActivePatterns() {
        return new ArrayList<>(patterns.values());
    }

    private void cleanupOldData() {
        Instant cutoff = Instant.now().minusSeconds(MAX_RETENTION_DAYS * 24 * 60 * 60);
        analyses.entrySet().removeIf(e -> e.getValue().getTimestamp().isBefore(cutoff));
    }
}
