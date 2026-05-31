package com.gogidix.aiservices.aimarketbasketanalysisservice.infrastructure.persistence.document;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * MongoDB document for MarketBasket.
 */
@Document(collection = "market_baskets")
public record MarketBasketDocument(

        @Id
        String id,

        @Indexed
        String name,

        String description,

        @Indexed
        BasketType segmentType,

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

    public MarketBasketDocument {
        if (customerIds == null) {
            customerIds = new HashSet<>();
        }
    }

    /**
     * Create a new MarketBasketDocument.
     */
    public static MarketBasketDocument create(
            String id,
            String name,
            String description,
            BasketType segmentType,
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
        return new MarketBasketDocument(
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
    public MarketBasketDocument withCustomerCount(int count) {
        return new MarketBasketDocument(
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
