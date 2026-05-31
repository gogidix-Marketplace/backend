package com.gogidix.management.executive.analytics.domain.repository;

import com.gogidix.management.executive.analytics.domain.model.DataFeed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DataFeed entities
 */
@Repository
public interface DataFeedRepository extends MongoRepository<DataFeed, String> {

    /**
     * Find data feeds by tenant ID
     */
    List<DataFeed> findByTenantId(String tenantId);

    /**
     * Find data feeds by tenant ID and active status
     */
    List<DataFeed> findByTenantIdAndActive(String tenantId, boolean active);

    /**
     * Find data feed by ID and tenant ID
     */
    Optional<DataFeed> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find active data feeds by tenant ID
     */
    List<DataFeed> findByTenantIdAndDeletedAtIsNull(String tenantId);
}
