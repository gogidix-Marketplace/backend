package com.gogidix.shared.warehousing.shipping.domain.repository;

import com.gogidix.shared.warehousing.shipping.domain.entity.Shipment;
import com.gogidix.shared.warehousing.shipping.domain.entity.Shipment.Carrier;
import com.gogidix.shared.warehousing.shipping.domain.entity.Shipment.ShipmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Shipment Repository with Multi-Tenant Support
 *
 * MongoDB-based repository for shipment management
 */
@Repository
public interface ShipmentRepository extends MongoRepository<Shipment, String> {

    /**
     * Find shipment by tracking number and tenant
     */
    Optional<Shipment> findByTenantIdAndTrackingNumber(String tenantId, String trackingNumber);

    /**
     * Find shipment by order number and tenant
     */
    List<Shipment> findByTenantIdAndOrderNumber(String tenantId, String orderNumber);

    /**
     * Find shipments by status and tenant
     */
    List<Shipment> findByTenantIdAndStatus(String tenantId, ShipmentStatus status);

    /**
     * Find shipments by carrier and tenant
     */
    List<Shipment> findByTenantIdAndCarrier(String tenantId, Carrier carrier);

    /**
     * Find shipments created within date range for tenant
     */
    List<Shipment> findByTenantIdAndCreatedAtBetween(String tenantId, LocalDateTime start, LocalDateTime end);

    /**
     * Find shipments delivered within date range for tenant
     */
    List<Shipment> findByTenantIdAndActualDeliveryBetween(String tenantId, LocalDateTime start, LocalDateTime end);

    /**
     * Find active shipments (not delivered or cancelled) for tenant
     */
    @Query("{'tenantId': ?0, 'status': {$nin: ['DELIVERED', 'CANCELLED']}}")
    List<Shipment> findActiveShipments(String tenantId);

	/**
	 * Find shipments requiring attention (exception status)
	 */
	@Query("{'tenantId': ?0, 'status': 'EXCEPTION'}")
	List<Shipment> findExceptionShipments(String tenantId);

	/**
	 * Count shipments by status for tenant
	 */
    long countByTenantIdAndStatus(String tenantId, ShipmentStatus status);

    /**
     * Find all shipments for a specific tenant
     */
    List<Shipment> findByTenantId(String tenantId);
}
