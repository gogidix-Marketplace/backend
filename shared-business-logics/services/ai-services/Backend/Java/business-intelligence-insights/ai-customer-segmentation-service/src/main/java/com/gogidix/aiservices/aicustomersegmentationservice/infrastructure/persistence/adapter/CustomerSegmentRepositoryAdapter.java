package com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.CustomerSegment;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.port.out.CustomerSegmentRepositoryPort;
import com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.persistence.document.CustomerSegmentDocument;
import com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.persistence.repository.SpringDataCustomerSegmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

/**
 * MongoDB adapter for CustomerSegment repository.
 */
@Repository
public class CustomerSegmentRepositoryAdapter implements CustomerSegmentRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(CustomerSegmentRepositoryAdapter.class);

    private final SpringDataCustomerSegmentRepository springRepository;

    public CustomerSegmentRepositoryAdapter(SpringDataCustomerSegmentRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public CustomerSegment save(CustomerSegment segment) {
        log.debug("Saving segment: {}", segment.getId());

        CustomerSegmentDocument document = toDocument(segment);
        CustomerSegmentDocument saved = springRepository.save(document);

        log.debug("Segment saved: {}", saved);
        return toDomain(saved);
    }

    @Override
    public Optional<CustomerSegment> findById(String id) {
        log.debug("Finding segment by ID: {}", id);

        return springRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        log.debug("Deleting segment by ID: {}", id);

        springRepository.deleteById(id);
        log.debug("Segment deleted: {}", id);
    }

    @Override
    public boolean existsById(String id) {
        return springRepository.existsById(id);
    }

    /**
     * Convert domain entity to document.
     */
    private CustomerSegmentDocument toDocument(CustomerSegment segment) {
        CustomerSegmentDocument.CriteriaEmbeddable criteriaEmbeddable = null;
        if (segment.getCriteria() != null) {
            criteriaEmbeddable = new CustomerSegmentDocument.CriteriaEmbeddable(
                    segment.getCriteria().getField(),
                    segment.getCriteria().getOperator().name(),
                    segment.getCriteria().getValue()
            );
        }

        return new CustomerSegmentDocument(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getSegmentType(),
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
    private CustomerSegment toDomain(CustomerSegmentDocument document) {
        SegmentCriteria criteria = null;
        if (document.criteria() != null) {
            criteria = SegmentCriteria.builder()
                    .type(com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria.CriteriaType.CUSTOM)
                    .operator(com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria.CriteriaOperator.EQUALS)
                    .field(document.criteria().field())
                    .value(document.criteria().value())
                    .build();
        }

        CustomerSegment segment = new CustomerSegment(
                document.tenantId(),
                document.name(),
                criteria != null ? criteria : SegmentCriteria.builder()
                        .type(com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria.CriteriaType.CUSTOM)
                        .operator(com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria.CriteriaOperator.EQUALS)
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
                com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentStatus.ACTIVE :
                com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentStatus.INACTIVE);
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
