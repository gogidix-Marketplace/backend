package com.gogidix.ecommerce.tracking.domain.repository;

import com.gogidix.ecommerce.tracking.domain.model.OrderTracking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderTrackingRepository extends MongoRepository<OrderTracking, String> {

    Optional<OrderTracking> findByTenantIdAndTrackingId(String tenantId, String trackingId);

    Optional<OrderTracking> findByTenantIdAndOrderId(String tenantId, String orderId);

    List<OrderTracking> findByTenantIdAndStatus(String tenantId, OrderTracking.TrackingStatus status);

    List<OrderTracking> findByTenantIdAndCustomerId(String tenantId, String customerId);
}
