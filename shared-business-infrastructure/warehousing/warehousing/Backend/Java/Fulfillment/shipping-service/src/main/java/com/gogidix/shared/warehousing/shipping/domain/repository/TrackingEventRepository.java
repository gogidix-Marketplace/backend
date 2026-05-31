package com.gogidix.shared.warehousing.shipping.domain.repository;

import com.gogidix.shared.warehousing.shipping.domain.entity.TrackingEvent;
import com.gogidix.shared.warehousing.shipping.domain.entity.TrackingEvent.TrackingEventType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Tracking Event Repository with Multi-Tenant Support
 *
 * MongoDB-based repository for tracking event management
 */
@Repository
public interface TrackingEventRepository extends MongoRepository<TrackingEvent, String> {

    /**
     * Find tracking events by shipment ID and tenant
     */
    List<TrackingEvent> findByTenantIdAndShipmentIdOrderByTimestampDesc(String tenantId, String shipmentId);

    /**
     * Find tracking events by tracking number and tenant
     */
    List<TrackingEvent> findByTenantIdAndTrackingNumberOrderByTimestampDesc(String tenantId, String trackingNumber);

    /**
     * Find tracking events by shipment ID, tenant, and event type
     */
    List<TrackingEvent> findByTenantIdAndShipmentIdAndEventType(String tenantId, String shipmentId, TrackingEventType eventType);

    /**
     * Find tracking events within date range for tenant
     */
    List<TrackingEvent> findByTenantIdAndTimestampBetweenOrderByTimestampDesc(String tenantId, LocalDateTime start, LocalDateTime end);

    /**
     * Find latest tracking event for a shipment
     */
    TrackingEvent findFirstByTenantIdAndShipmentIdOrderByTimestampDesc(String tenantId, String shipmentId);
}
