package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.FraudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MongoDB implementation of the FraudRepository port.
 * This adapter maps between domain models and MongoDB entities.
 *
 * Only created when MongoDB is configured.
 */
@Repository("mongoFraudRepositoryAdapter")
@Primary
@ConditionalOnProperty(
    name = "spring.data.mongodb.auto-index-creation",
    havingValue = "true",
    matchIfMissing = false
)
public class MongoFraudRepositoryAdapter implements FraudRepository {

    private static final Logger log = LoggerFactory.getLogger(MongoFraudRepositoryAdapter.class);

    private static final int MAX_RETENTION_DAYS = 180;

    private final SpringDataFraudAnalysisResultRepository analysisRepository;
    private final SpringDataFraudPatternRepository patternRepository;

    @Autowired
    public MongoFraudRepositoryAdapter(
            SpringDataFraudAnalysisResultRepository analysisRepository,
            SpringDataFraudPatternRepository patternRepository) {
        this.analysisRepository = analysisRepository;
        this.patternRepository = patternRepository;
        log.info("MongoFraudRepositoryAdapter initialized with MongoDB persistence");
    }

    @Override
    public void saveAnalysisResult(FraudAnalysisResult result) {
        log.debug("Saving fraud analysis result: {}", result.getAnalysisId());

        // Check if analysis already exists
        FraudAnalysisResultEntity existingEntity = analysisRepository.findByAnalysisId(result.getAnalysisId());

        if (existingEntity != null) {
            // Update existing
            existingEntity.updateFrom(result);
            analysisRepository.save(existingEntity);
            log.debug("Updated existing fraud analysis result: {}", result.getAnalysisId());
        } else {
            // Create new
            FraudAnalysisResultEntity entity = new FraudAnalysisResultEntity(result);
            analysisRepository.save(entity);
            log.debug("Saved new fraud analysis result: {}", result.getAnalysisId());
        }

        // Cleanup old data asynchronously
        cleanupOldData();
    }

    @Override
    public Optional<FraudAnalysisResult> findAnalysisById(String analysisId) {
        log.debug("Finding fraud analysis by ID: {}", analysisId);

        FraudAnalysisResultEntity entity = analysisRepository.findByAnalysisId(analysisId);

        if (entity == null) {
            log.debug("No fraud analysis found with ID: {}", analysisId);
            return Optional.empty();
        }

        return Optional.of(entity.toDomainModel());
    }

    @Override
    public List<FraudAnalysisResult> findByUserId(String userId, int limit) {
        log.debug("Finding fraud analyses for user: {} with limit: {}", userId, limit);

        List<FraudAnalysisResultEntity> entities;

        if (limit <= 0) {
            // No limit - return all
            entities = analysisRepository.findByUserIdOrderByTimestampDesc(userId);
        } else {
            // Apply limit using pagination
            entities = analysisRepository.findByUserIdOrderByTimestampDesc(userId, PageRequest.of(0, limit));
        }

        return entities.stream()
                .map(FraudAnalysisResultEntity::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public void savePattern(FraudPattern pattern) {
        log.debug("Saving fraud pattern: {}", pattern.getPatternId());

        // Check if pattern already exists
        FraudPatternEntity existingEntity = patternRepository.findByPatternId(pattern.getPatternId());

        if (existingEntity != null) {
            // Update existing and increment occurrence
            existingEntity.updateFrom(pattern);
            existingEntity.incrementOccurrence();
            patternRepository.save(existingEntity);
            log.debug("Updated existing fraud pattern: {}", pattern.getPatternId());
        } else {
            // Create new
            FraudPatternEntity entity = new FraudPatternEntity(pattern);
            patternRepository.save(entity);
            log.debug("Saved new fraud pattern: {}", pattern.getPatternId());
        }
    }

    @Override
    public List<FraudPattern> getActivePatterns() {
        log.debug("Finding all active fraud patterns");

        List<FraudPatternEntity> entities = patternRepository.findByIsActiveTrueOrderByLastSeenDesc();

        return entities.stream()
                .map(FraudPatternEntity::toDomainModel)
                .collect(Collectors.toList());
    }

    /**
     * Find active patterns for a specific tenant.
     */
    public List<FraudPattern> getActivePatternsByTenant(String tenantId) {
        log.debug("Finding active fraud patterns for tenant: {}", tenantId);

        List<FraudPatternEntity> entities = patternRepository.findByTenantIdAndIsActiveTrueOrderByLastSeenDesc(tenantId);

        return entities.stream()
                .map(FraudPatternEntity::toDomainModel)
                .collect(Collectors.toList());
    }

    /**
     * Find fraud analyses by tenant.
     */
    public List<FraudAnalysisResult> findByTenantId(String tenantId, int limit) {
        log.debug("Finding fraud analyses for tenant: {} with limit: {}", tenantId, limit);

        List<FraudAnalysisResultEntity> entities;

        if (limit <= 0) {
            entities = analysisRepository.findByTenantIdOrderByTimestampDesc(tenantId);
        } else {
            entities = analysisRepository.findByTenantIdOrderByTimestampDesc(tenantId, PageRequest.of(0, limit));
        }

        return entities.stream()
                .map(FraudAnalysisResultEntity::toDomainModel)
                .collect(Collectors.toList());
    }

    /**
     * Delete a fraud pattern by ID.
     */
    public boolean deletePattern(String patternId) {
        log.debug("Deleting fraud pattern: {}", patternId);

        FraudPatternEntity entity = patternRepository.findByPatternId(patternId);

        if (entity != null) {
            entity.deactivate();
            patternRepository.save(entity);
            log.debug("Deactivated fraud pattern: {}", patternId);
            return true;
        }

        return false;
    }

    /**
     * Get statistics for a tenant.
     */
    public TenantStats getTenantStats(String tenantId) {
        long analysisCount = analysisRepository.countByTenantId(tenantId);
        long patternCount = patternRepository.countByTenantIdAndIsActiveTrue(tenantId);

        return new TenantStats(tenantId, analysisCount, patternCount);
    }

    /**
     * Cleanup old data based on retention policy.
     */
    private void cleanupOldData() {
        try {
            Instant cutoff = Instant.now().minusSeconds(MAX_RETENTION_DAYS * 24L * 60 * 60);

            // Find and delete old analyses
            List<FraudAnalysisResultEntity> oldAnalyses = analysisRepository.findByTimestampBefore(cutoff);

            if (!oldAnalyses.isEmpty()) {
                log.info("Cleaning up {} old fraud analyses older than {}", oldAnalyses.size(), cutoff);
                analysisRepository.deleteByTimestampBefore(cutoff);
            }
        } catch (Exception e) {
            log.error("Error during cleanup of old data", e);
        }
    }

    /**
     * Statistics record for tenant.
     */
    public record TenantStats(String tenantId, long analysisCount, long patternCount) {
    }
}
