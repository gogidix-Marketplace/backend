package com.gogidix.ecommerce.realtime.domain.repository;

import com.gogidix.ecommerce.realtime.domain.model.RealtimeTracking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RealtimeTrackingRepository extends MongoRepository<RealtimeTracking, String> {

    Optional<RealtimeTracking> findByTenantIdAndTrackingId(String tenantId, String trackingId);

    Optional<RealtimeTracking> findByTenantIdAndOrderId(String tenantId, String orderId);

    Optional<RealtimeTracking> findByTenantIdAndDeviceId(String tenantId, String deviceId);
}
