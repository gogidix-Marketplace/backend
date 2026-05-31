package com.gogidix.aiservices.aisalesforecastingservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.SalesForecast;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;
import com.gogidix.aiservices.aisalesforecastingservice.domain.port.out.SalesForecastingRepositoryPort;
import com.gogidix.aiservices.aisalesforecastingservice.infrastructure.persistence.document.SalesForecastingDocument;
import com.gogidix.aiservices.aisalesforecastingservice.infrastructure.persistence.repository.SpringDataSalesForecastingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

/**
 * MongoDB adapter for SalesForecast repository.
 */
@Repository
public class SalesForecastingRepositoryAdapter implements SalesForecastingRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(SalesForecastingRepositoryAdapter.class);

    private final SpringDataSalesForecastingRepository springRepository;

    public SalesForecastingRepositoryAdapter(SpringDataSalesForecastingRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public SalesForecast save(SalesForecast segment) {
        log.debug("Saving segment: {}", segment.getId());

        SalesForecastingDocument document = toDocument(segment);
        SalesForecastingDocument saved = springRepository.save(document);

        log.debug("Forecast saved: {}", saved);
        return toDomain(saved);
    }

    @Override
    public Optional<SalesForecast> findById(String id) {
        log.debug("Finding segment by ID: {}", id);

        return springRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        log.debug("Deleting segment by ID: {}", id);

        springRepository.deleteById(id);
        log.debug("Forecast deleted: {}", id);
    }

    @Override
    public boolean existsById(String id) {
        return springRepository.existsById(id);
    }

    /**
     * Convert domain entity to document.
     */
    private SalesForecastingDocument toDocument(SalesForecast segment) {
        SalesForecastingDocument.CriteriaEmbeddable criteriaEmbeddable = null;
        if (segment.getCriteria() != null) {
            criteriaEmbeddable = new SalesForecastingDocument.CriteriaEmbeddable(
                    segment.getCriteria().getField(),
                    segment.getCriteria().getOperator().name(),
                    segment.getCriteria().getValue()
            );
        }

        return new SalesForecastingDocument(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getForecastType(),
                criteriaEmbeddable,
                segment.getForecastModelIds() != null ? java.util.Set.copyOf(segment.getForecastModelIds()) : java.util.Set.of(),
                segment.getForecastModelCount().intValue(),
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
    private SalesForecast toDomain(SalesForecastingDocument document) {
        ForecastCriteria criteria = null;
        if (document.criteria() != null) {
            criteria = ForecastCriteria.builder()
                    .type(com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria.CriteriaType.CUSTOM)
                    .operator(com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria.CriteriaOperator.EQUALS)
                    .field(document.criteria().field())
                    .value(document.criteria().value())
                    .build();
        }

        SalesForecast segment = new SalesForecast(
                document.tenantId(),
                document.name(),
                criteria != null ? criteria : ForecastCriteria.builder()
                        .type(com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria.CriteriaType.CUSTOM)
                        .operator(com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria.CriteriaOperator.EQUALS)
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
                com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastStatus.ACTIVE :
                com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastStatus.INACTIVE);
        setProtectedField(segment, "createdAt", document.createdAt());
        setProtectedField(segment, "updatedAt", document.updatedAt());
        setProtectedField(segment, "lastAnalyzedAt", Instant.now());

        // Add customer IDs if present
        if (document.customerIds() != null && !document.customerIds().isEmpty()) {
            segment.addForecastModels(new java.util.ArrayList<>(document.customerIds()));
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
