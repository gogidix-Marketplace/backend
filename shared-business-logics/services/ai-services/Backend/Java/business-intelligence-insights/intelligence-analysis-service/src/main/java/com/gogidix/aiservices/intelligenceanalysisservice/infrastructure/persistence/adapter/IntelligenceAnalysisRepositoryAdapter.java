package com.gogidix.aiservices.intelligenceanalysisservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.IntelligenceAnalysis;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.port.out.IntelligenceAnalysisRepositoryPort;
import com.gogidix.aiservices.intelligenceanalysisservice.infrastructure.persistence.document.IntelligenceAnalysisDocument;
import com.gogidix.aiservices.intelligenceanalysisservice.infrastructure.persistence.repository.SpringDataIntelligenceAnalysisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

/**
 * MongoDB adapter for IntelligenceAnalysis repository.
 */
@Repository
public class IntelligenceAnalysisRepositoryAdapter implements IntelligenceAnalysisRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(IntelligenceAnalysisRepositoryAdapter.class);

    private final SpringDataIntelligenceAnalysisRepository springRepository;

    public IntelligenceAnalysisRepositoryAdapter(SpringDataIntelligenceAnalysisRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public IntelligenceAnalysis save(IntelligenceAnalysis segment) {
        log.debug("Saving segment: {}", segment.getId());

        IntelligenceAnalysisDocument document = toDocument(segment);
        IntelligenceAnalysisDocument saved = springRepository.save(document);

        log.debug("Analysis saved: {}", saved);
        return toDomain(saved);
    }

    @Override
    public Optional<IntelligenceAnalysis> findById(String id) {
        log.debug("Finding segment by ID: {}", id);

        return springRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        log.debug("Deleting segment by ID: {}", id);

        springRepository.deleteById(id);
        log.debug("Analysis deleted: {}", id);
    }

    @Override
    public boolean existsById(String id) {
        return springRepository.existsById(id);
    }

    /**
     * Convert domain entity to document.
     */
    private IntelligenceAnalysisDocument toDocument(IntelligenceAnalysis segment) {
        IntelligenceAnalysisDocument.CriteriaEmbeddable criteriaEmbeddable = null;
        if (segment.getCriteria() != null) {
            criteriaEmbeddable = new IntelligenceAnalysisDocument.CriteriaEmbeddable(
                    segment.getCriteria().getField(),
                    segment.getCriteria().getOperator().name(),
                    segment.getCriteria().getValue()
            );
        }

        return new IntelligenceAnalysisDocument(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getAnalysisType(),
                criteriaEmbeddable,
                segment.getIntelligenceReportIds() != null ? java.util.Set.copyOf(segment.getIntelligenceReportIds()) : java.util.Set.of(),
                segment.getIntelligenceReportCount().intValue(),
                segment.isActive(),
                segment.getTenantId(),
                "system",
                segment.getCreatedAt() != null ? segment.getCreatedAt() : Instant.now(),
                "system",
                segment.getUpdatedAt() != null ? segment.getUpdatedAt() : Instant.now(),
                0L
        );
    }

    /**
     * Convert document to domain entity using reflection to set protected fields.
     */
    private IntelligenceAnalysis toDomain(IntelligenceAnalysisDocument document) {
        AnalysisCriteria criteria = null;
        if (document.criteria() != null) {
            criteria = AnalysisCriteria.builder()
                    .type(com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria.CriteriaType.CUSTOM)
                    .operator(com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria.CriteriaOperator.EQUALS)
                    .field(document.criteria().field())
                    .value(document.criteria().value())
                    .build();
        }

        IntelligenceAnalysis segment = new IntelligenceAnalysis(
                document.tenantId(),
                document.name(),
                criteria != null ? criteria : AnalysisCriteria.builder()
                        .type(com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria.CriteriaType.CUSTOM)
                        .operator(com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria.CriteriaOperator.EQUALS)
                        .field("default")
                        .value("default")
                        .build()
        );

        // Use reflection to set protected fields
        setProtectedField(segment, "id", document.id());
        setProtectedField(segment, "description", document.description());
        setProtectedField(segment, "segmentType", document.segmentType());
        setProtectedField(segment, "customerCount", (long) document.customerCount());
        setProtectedField(segment, "status", document.active() ?
                com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisStatus.ACTIVE :
                com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisStatus.INACTIVE);
        setProtectedField(segment, "createdAt", document.createdAt());
        setProtectedField(segment, "updatedAt", document.updatedAt());
        setProtectedField(segment, "lastAnalyzedAt", Instant.now());

        // Add customer IDs if present
        if (document.customerIds() != null && !document.customerIds().isEmpty()) {
            segment.addIntelligenceReports(new java.util.ArrayList<>(document.customerIds()));
        }

        return segment;
    }

    /**
     * Use reflection to set protected field.
     */
    private void setProtectedField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("Could not set field '{}' on {}: {}", fieldName, target.getClass().getSimpleName(), e.getMessage());
        }
    }
}
