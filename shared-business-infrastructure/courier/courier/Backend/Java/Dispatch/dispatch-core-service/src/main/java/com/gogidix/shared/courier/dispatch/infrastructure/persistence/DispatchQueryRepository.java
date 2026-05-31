package com.gogidix.shared.courier.dispatch.infrastructure.persistence;

import com.gogidix.shared.courier.dispatch.domain.entity.DispatchOrder;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import com.gogidix.shared.courier.dispatch.domain.repository.DispatchOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Custom query repository implementation for Dispatch Order
 * Provides complex queries beyond standard MongoRepository
 */
@Slf4j
@Repository
public class DispatchQueryRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DispatchOrderRepository dispatchOrderRepository;

    /**
     * Find unassigned dispatches by priority and location proximity
     */
    public List<DispatchOrder> findUnassignedByPriorityAndLocation(
            String tenantId,
            GeoJsonPoint location,
            double maxDistanceMeters,
            int limit) {

        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("status").is(DispatchStatus.PENDING));
        query.addCriteria(Criteria.where("assignedDriverId").exists(false));
        query.addCriteria(Criteria.where("pickupLocation").near(location).maxDistance(maxDistanceMeters));
        query.with(Pageable.ofSize(limit));

        return mongoTemplate.find(query, DispatchOrder.class);
    }

    /**
     * Find dispatches by customer and date range
     */
    public List<DispatchOrder> findByCustomerAndDateRange(
            String tenantId,
            String customerId,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("customerId").is(customerId));
        query.addCriteria(Criteria.where("createdAt").gte(startDate).lte(endDate));

        return mongoTemplate.find(query, DispatchOrder.class);
    }

    /**
     * Find dispatches by multiple criteria with pagination
     */
    public List<DispatchOrder> findDispatchWithPagination(
            String tenantId,
            DispatchStatus status,
            String customerId,
            Pageable pageable) {

        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));

        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        if (customerId != null) {
            query.addCriteria(Criteria.where("customerId").is(customerId));
        }

        query.with(pageable);

        return mongoTemplate.find(query, DispatchOrder.class);
    }

    /**
     * Find high priority pending dispatches
     */
    public List<DispatchOrder> findHighPriorityPending(String tenantId, int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("status").is(DispatchStatus.PENDING));
        query.addCriteria(Criteria.where("priority").lte(2));
        query.with(Pageable.ofSize(limit));

        return mongoTemplate.find(query, DispatchOrder.class);
    }

    /**
     * Find dispatches needing driver assignment
     */
    public List<DispatchOrder> findNeedingAssignment(String tenantId, LocalDateTime beforeTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("status").in(
                List.of(DispatchStatus.PENDING, DispatchStatus.CONFIRMED)));
        query.addCriteria(Criteria.where("estimatedPickupTime").lte(beforeTime));

        return mongoTemplate.find(query, DispatchOrder.class);
    }

    /**
     * Find active dispatches for driver
     */
    public List<DispatchOrder> findActiveDispatchesForDriver(String tenantId, String driverId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("assignedDriverId").is(driverId));
        query.addCriteria(Criteria.where("status").in(
                List.of(DispatchStatus.ASSIGNED, DispatchStatus.PICKUP_IN_PROGRESS,
                        DispatchStatus.PICKED_UP, DispatchStatus.IN_TRANSIT,
                        DispatchStatus.DELIVERY_IN_PROGRESS)));

        return mongoTemplate.find(query, DispatchOrder.class);
    }
}
