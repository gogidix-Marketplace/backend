package com.gogidix.shared.warehousing.cyclecounting.domain.repository;

import com.gogidix.shared.warehousing.cyclecounting.domain.entity.CycleCount;
import com.gogidix.shared.warehousing.cyclecounting.domain.entity.CycleCount.CountStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Cycle Count Repository
 */
@Repository
public interface CycleCountRepository extends MongoRepository<CycleCount, String> {

    /**
     * Find by tenant and warehouse
     */
    List<CycleCount> findByTenantIdAndWarehouseIdOrderByScheduledDateDesc(
            String tenantId, String warehouseId);

    /**
     * Find by tenant, warehouse, and status
     */
    List<CycleCount> findByTenantIdAndWarehouseIdAndStatus(
            String tenantId, String warehouseId, CountStatus status);

    /**
     * Find by count number
     */
    Optional<CycleCount> findByTenantIdAndCountNumber(String tenantId, String countNumber);

    /**
     * Find overdue counts
     */
    List<CycleCount> findByTenantIdAndStatusAndDueDateBefore(
            String tenantId, CountStatus status, LocalDateTime dueDate);

    /**
     * Find pending counts
     */
    List<CycleCount> findByTenantIdAndStatusOrderByScheduledDateAsc(
            String tenantId, CountStatus status);

    /**
     * Find counts assigned to user
     */
    List<CycleCount> findByTenantIdAndAssignedToAndStatusNot(
            String tenantId, String userId, CountStatus status);
}
