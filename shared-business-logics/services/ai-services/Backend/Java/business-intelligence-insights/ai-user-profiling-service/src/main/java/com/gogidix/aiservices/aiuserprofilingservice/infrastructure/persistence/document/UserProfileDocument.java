package com.gogidix.aiservices.aiuserprofilingservice.infrastructure.persistence.document;

import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * MongoDB document for UserProfile.
 */
@Document(collection = "customer_segments")
public record UserProfileDocument(

        @Id
        String id,

        @Indexed
        String name,

        String description,

        @Indexed
        ProfileType segmentType,

        CriteriaEmbeddable criteria,

        @Indexed
        Set<String> customerIds,

        @Indexed
        int customerCount,

        @Indexed
        boolean active,

        @Indexed
        String tenantId,

        @Indexed
        String createdBy,

        @Indexed
        Instant createdAt,

        @Indexed
        String lastModifiedBy,

        @Indexed
        Instant updatedAt,

        @Indexed
        Long version

) {

    public UserProfileDocument {
        if (customerIds == null) {
            customerIds = new HashSet<>();
        }
    }

    /**
     * Create a new UserProfileDocument.
     */
    public static UserProfileDocument create(
            String id,
            String name,
            String description,
            ProfileType segmentType,
            CriteriaEmbeddable criteria,
            Set<String> customerIds,
            boolean active,
            String tenantId,
            String createdBy,
            Instant createdAt,
            String lastModifiedBy,
            Instant updatedAt,
            Long version
    ) {
        return new UserProfileDocument(
                id,
                name,
                description,
                segmentType,
                criteria,
                customerIds,
                customerIds != null ? customerIds.size() : 0,
                active,
                tenantId,
                createdBy,
                createdAt,
                lastModifiedBy,
                updatedAt,
                version
        );
    }

    /**
     * Update customer count.
     */
    public UserProfileDocument withUserCount(int count) {
        return new UserProfileDocument(
                id, name, description, segmentType, criteria,
                customerIds, count, active, tenantId,
                createdBy, createdAt, lastModifiedBy, updatedAt, version
        );
    }

    /**
     * Embedded criteria object.
     */
    public record CriteriaEmbeddable(
            String field,
            String operator,
            Object value
    ) {
    }
}
