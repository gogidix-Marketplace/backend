package com.gogidix.shared.courier.ecommerce.domain.repository;

import java.util.List;
import java.util.Optional;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;

public interface EcommerceAssignmentRepository {
    EcommerceCourierAssignment save(EcommerceCourierAssignment assignment);
    Optional<EcommerceCourierAssignment> findById(String id);
    Optional<EcommerceCourierAssignment> findByOrderIdAndSubOrderId(String orderId, String subOrderId);
    Optional<EcommerceCourierAssignment> findByTrackingId(String trackingId);
    List<EcommerceCourierAssignment> findByOrderId(String orderId);
    List<EcommerceCourierAssignment> findByPickupZoneIdAndStatus(String zoneId, EcommerceCourierAssignment.AssignmentStatus status);
    List<EcommerceCourierAssignment> findByDeliveryZoneIdAndStatus(String zoneId, EcommerceCourierAssignment.AssignmentStatus status);
}
