package com.gogidix.aiservices.aimarketbasketanalysisservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.MarketBasket;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.port.out.MarketBasketRepositoryPort;
import com.gogidix.aiservices.aimarketbasketanalysisservice.infrastructure.persistence.document.MarketBasketDocument;
import com.gogidix.aiservices.aimarketbasketanalysisservice.infrastructure.persistence.repository.SpringDataMarketBasketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

/**
 * MongoDB adapter for MarketBasket repository.
 */
@Repository
public class MarketBasketRepositoryAdapter implements MarketBasketRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(MarketBasketRepositoryAdapter.class);

    private final SpringDataMarketBasketRepository springRepository;

    public MarketBasketRepositoryAdapter(SpringDataMarketBasketRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public MarketBasket save(MarketBasket segment) {
        log.debug("Saving segment: {}", segment.getId());

        MarketBasketDocument document = toDocument(segment);
        MarketBasketDocument saved = springRepository.save(document);

        log.debug("Basket saved: {}", saved);
        return toDomain(saved);
    }

    @Override
    public Optional<MarketBasket> findById(String id) {
        log.debug("Finding segment by ID: {}", id);

        return springRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        log.debug("Deleting segment by ID: {}", id);

        springRepository.deleteById(id);
        log.debug("Basket deleted: {}", id);
    }

    @Override
    public boolean existsById(String id) {
        return springRepository.existsById(id);
    }

    /**
     * Convert domain entity to document.
     */
    private MarketBasketDocument toDocument(MarketBasket segment) {
        MarketBasketDocument.CriteriaEmbeddable criteriaEmbeddable = null;
        if (segment.getCriteria() != null) {
            criteriaEmbeddable = new MarketBasketDocument.CriteriaEmbeddable(
                    segment.getCriteria().getField(),
                    segment.getCriteria().getOperator().name(),
                    segment.getCriteria().getValue()
            );
        }

        return new MarketBasketDocument(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getBasketType(),
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
    private MarketBasket toDomain(MarketBasketDocument document) {
        BasketCriteria criteria = null;
        if (document.criteria() != null) {
            criteria = BasketCriteria.builder()
                    .type(com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria.CriteriaType.CUSTOM)
                    .operator(com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria.CriteriaOperator.EQUALS)
                    .field(document.criteria().field())
                    .value(document.criteria().value())
                    .build();
        }

        MarketBasket segment = new MarketBasket(
                document.tenantId(),
                document.name(),
                criteria != null ? criteria : BasketCriteria.builder()
                        .type(com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria.CriteriaType.CUSTOM)
                        .operator(com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria.CriteriaOperator.EQUALS)
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
                com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketStatus.ACTIVE :
                com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketStatus.INACTIVE);
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
