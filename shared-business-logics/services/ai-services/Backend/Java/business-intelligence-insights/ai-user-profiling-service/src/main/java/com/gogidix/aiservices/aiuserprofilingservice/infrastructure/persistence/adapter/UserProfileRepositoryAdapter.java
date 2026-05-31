package com.gogidix.aiservices.aiuserprofilingservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.aiuserprofilingservice.domain.model.UserProfile;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria;
import com.gogidix.aiservices.aiuserprofilingservice.domain.port.out.UserProfileRepositoryPort;
import com.gogidix.aiservices.aiuserprofilingservice.infrastructure.persistence.document.UserProfileDocument;
import com.gogidix.aiservices.aiuserprofilingservice.infrastructure.persistence.repository.SpringDataUserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

/**
 * MongoDB adapter for UserProfile repository.
 */
@Repository
public class UserProfileRepositoryAdapter implements UserProfileRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(UserProfileRepositoryAdapter.class);

    private final SpringDataUserProfileRepository springRepository;

    public UserProfileRepositoryAdapter(SpringDataUserProfileRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public UserProfile save(UserProfile segment) {
        log.debug("Saving segment: {}", segment.getId());

        UserProfileDocument document = toDocument(segment);
        UserProfileDocument saved = springRepository.save(document);

        log.debug("Profile saved: {}", saved);
        return toDomain(saved);
    }

    @Override
    public Optional<UserProfile> findById(String id) {
        log.debug("Finding segment by ID: {}", id);

        return springRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        log.debug("Deleting segment by ID: {}", id);

        springRepository.deleteById(id);
        log.debug("Profile deleted: {}", id);
    }

    @Override
    public boolean existsById(String id) {
        return springRepository.existsById(id);
    }

    /**
     * Convert domain entity to document.
     */
    private UserProfileDocument toDocument(UserProfile segment) {
        UserProfileDocument.CriteriaEmbeddable criteriaEmbeddable = null;
        if (segment.getCriteria() != null) {
            criteriaEmbeddable = new UserProfileDocument.CriteriaEmbeddable(
                    segment.getCriteria().getField(),
                    segment.getCriteria().getOperator().name(),
                    segment.getCriteria().getValue()
            );
        }

        return new UserProfileDocument(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getProfileType(),
                criteriaEmbeddable,
                segment.getUserIds() != null ? java.util.Set.copyOf(segment.getUserIds()) : java.util.Set.of(),
                segment.getUserCount().intValue(),
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
    private UserProfile toDomain(UserProfileDocument document) {
        ProfileCriteria criteria = null;
        if (document.criteria() != null) {
            criteria = ProfileCriteria.builder()
                    .type(com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria.CriteriaOperator.EQUALS)
                    .field(document.criteria().field())
                    .value(document.criteria().value())
                    .build();
        }

        UserProfile segment = new UserProfile(
                document.tenantId(),
                document.name(),
                criteria != null ? criteria : ProfileCriteria.builder()
                        .type(com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria.CriteriaType.CUSTOM)
                        .operator(com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria.CriteriaOperator.EQUALS)
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
                com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileStatus.ACTIVE :
                com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileStatus.INACTIVE);
        setProtectedField(segment, "createdAt", document.createdAt());
        setProtectedField(segment, "updatedAt", document.updatedAt());
        setProtectedField(segment, "lastAnalyzedAt", Instant.now());

        // Add customer IDs if present
        if (document.customerIds() != null && !document.customerIds().isEmpty()) {
            segment.addUsers(new java.util.ArrayList<>(document.customerIds()));
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
