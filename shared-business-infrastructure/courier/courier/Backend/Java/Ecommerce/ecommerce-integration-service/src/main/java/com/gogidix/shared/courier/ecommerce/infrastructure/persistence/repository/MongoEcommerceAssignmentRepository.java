package com.gogidix.shared.courier.ecommerce.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;

@Repository
public interface MongoEcommerceAssignmentRepository extends MongoRepository<EcommerceCourierAssignment, String> {

    Optional<EcommerceCourierAssignment> findByOrderIdAndSubOrderId(String orderId, String subOrderId);

    Optional<EcommerceCourierAssignment> findByTrackingId(String trackingId);

    List<EcommerceCourierAssignment> findByOrderId(String orderId);

    @Query("{ 'pickupZoneId': ?0, 'status': ?1 }")
    List<EcommerceCourierAssignment> findByPickupZoneIdAndStatus(String zoneId, String status);

    @Query("{ 'deliveryZoneId': ?0, 'status': ?1 }")
    List<EcommerceCourierAssignment> findByDeliveryZoneIdAndStatus(String zoneId, String status);
}
