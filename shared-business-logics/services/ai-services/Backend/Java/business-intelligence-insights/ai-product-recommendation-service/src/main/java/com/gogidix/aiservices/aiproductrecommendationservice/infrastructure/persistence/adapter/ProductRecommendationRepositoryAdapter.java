package com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductRecommendation;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.port.out.ProductRecommendationRepositoryPort;
import com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.persistence.document.ProductRecommendationDocument;
import com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.persistence.repository.SpringDataProductRecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

/**
 * MongoDB adapter for ProductRecommendation repository.
 */
@Repository
public class ProductRecommendationRepositoryAdapter implements ProductRecommendationRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(ProductRecommendationRepositoryAdapter.class);

    private final SpringDataProductRecommendationRepository springRepository;

    public ProductRecommendationRepositoryAdapter(SpringDataProductRecommendationRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public ProductRecommendation save(ProductRecommendation segment) {
        log.debug("Saving segment: {}", segment.getId());

        ProductRecommendationDocument document = toDocument(segment);
        ProductRecommendationDocument saved = springRepository.save(document);

        log.debug("Recommendation saved: {}", saved);
        return toDomain(saved);
    }

    @Override
    public Optional<ProductRecommendation> findById(String id) {
        log.debug("Finding segment by ID: {}", id);

        return springRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        log.debug("Deleting segment by ID: {}", id);

        springRepository.deleteById(id);
        log.debug("Recommendation deleted: {}", id);
    }

    @Override
    public boolean existsById(String id) {
        return springRepository.existsById(id);
    }

    /**
     * Convert domain entity to document.
     */
    private ProductRecommendationDocument toDocument(ProductRecommendation segment) {
        ProductRecommendationDocument.CriteriaEmbeddable criteriaEmbeddable = null;
        if (segment.getCriteria() != null) {
            criteriaEmbeddable = new ProductRecommendationDocument.CriteriaEmbeddable(
                    segment.getCriteria().getField(),
                    segment.getCriteria().getOperator().name(),
                    segment.getCriteria().getValue()
            );
        }

        return new ProductRecommendationDocument(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getRecommendationType(),
                criteriaEmbeddable,
                segment.getProductIds() != null ? java.util.Set.copyOf(segment.getProductIds()) : java.util.Set.of(),
                segment.getProductCount().intValue(),
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
    private ProductRecommendation toDomain(ProductRecommendationDocument document) {
        RecommendationCriteria criteria = null;
        if (document.criteria() != null) {
            criteria = RecommendationCriteria.builder()
                    .type(com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field(document.criteria().field())
                    .value(document.criteria().value())
                    .build();
        }

        ProductRecommendation segment = new ProductRecommendation(
                document.tenantId(),
                document.name(),
                criteria != null ? criteria : RecommendationCriteria.builder()
                        .type(com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria.CriteriaType.CUSTOM)
                        .operator(com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria.CriteriaOperator.EQUALS)
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
                com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationStatus.ACTIVE :
                com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationStatus.INACTIVE);
        setProtectedField(segment, "createdAt", document.createdAt());
        setProtectedField(segment, "updatedAt", document.updatedAt());
        setProtectedField(segment, "lastAnalyzedAt", Instant.now());

        // Add customer IDs if present
        if (document.customerIds() != null && !document.customerIds().isEmpty()) {
            segment.addProducts(new java.util.ArrayList<>(document.customerIds()));
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
