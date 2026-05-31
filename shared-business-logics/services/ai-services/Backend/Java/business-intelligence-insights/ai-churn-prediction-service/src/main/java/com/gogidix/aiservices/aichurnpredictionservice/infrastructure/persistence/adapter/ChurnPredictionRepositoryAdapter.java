package com.gogidix.aiservices.aichurnpredictionservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.ChurnPrediction;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria;
import com.gogidix.aiservices.aichurnpredictionservice.domain.port.out.ChurnPredictionRepositoryPort;
import com.gogidix.aiservices.aichurnpredictionservice.infrastructure.persistence.document.ChurnPredictionDocument;
import com.gogidix.aiservices.aichurnpredictionservice.infrastructure.persistence.repository.SpringDataChurnPredictionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

/**
 * MongoDB adapter for ChurnPrediction repository.
 */
@Repository
public class ChurnPredictionRepositoryAdapter implements ChurnPredictionRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(ChurnPredictionRepositoryAdapter.class);

    private final SpringDataChurnPredictionRepository springRepository;

    public ChurnPredictionRepositoryAdapter(SpringDataChurnPredictionRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public ChurnPrediction save(ChurnPrediction segment) {
        log.debug("Saving segment: {}", segment.getId());

        ChurnPredictionDocument document = toDocument(segment);
        ChurnPredictionDocument saved = springRepository.save(document);

        log.debug("Prediction saved: {}", saved);
        return toDomain(saved);
    }

    @Override
    public Optional<ChurnPrediction> findById(String id) {
        log.debug("Finding segment by ID: {}", id);

        return springRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        log.debug("Deleting segment by ID: {}", id);

        springRepository.deleteById(id);
        log.debug("Prediction deleted: {}", id);
    }

    @Override
    public boolean existsById(String id) {
        return springRepository.existsById(id);
    }

    /**
     * Convert domain entity to document.
     */
    private ChurnPredictionDocument toDocument(ChurnPrediction segment) {
        ChurnPredictionDocument.CriteriaEmbeddable criteriaEmbeddable = null;
        if (segment.getCriteria() != null) {
            criteriaEmbeddable = new ChurnPredictionDocument.CriteriaEmbeddable(
                    segment.getCriteria().getField(),
                    segment.getCriteria().getOperator().name(),
                    segment.getCriteria().getValue()
            );
        }

        return new ChurnPredictionDocument(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getPredictionType(),
                criteriaEmbeddable,
                segment.getCustomerIds() != null ? java.util.Set.copyOf(segment.getCustomerIds()) : java.util.Set.of(),
                segment.getCustomerCount().intValue(),
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
    private ChurnPrediction toDomain(ChurnPredictionDocument document) {
        PredictionCriteria criteria = null;
        if (document.criteria() != null) {
            criteria = PredictionCriteria.builder()
                    .type(com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria.CriteriaType.CUSTOM)
                    .operator(com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria.CriteriaOperator.EQUALS)
                    .field(document.criteria().field())
                    .value(document.criteria().value())
                    .build();
        }

        ChurnPrediction segment = new ChurnPrediction(
                document.tenantId(),
                document.name(),
                criteria != null ? criteria : PredictionCriteria.builder()
                        .type(com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria.CriteriaType.CUSTOM)
                        .operator(com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria.CriteriaOperator.EQUALS)
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
                com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionStatus.ACTIVE :
                com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionStatus.INACTIVE);
        setProtectedField(segment, "createdAt", document.createdAt());
        setProtectedField(segment, "updatedAt", document.updatedAt());
        setProtectedField(segment, "lastAnalyzedAt", Instant.now());

        // Add customer IDs if present
        if (document.customerIds() != null && !document.customerIds().isEmpty()) {
            segment.addCustomers(new java.util.ArrayList<>(document.customerIds()));
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
