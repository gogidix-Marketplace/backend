package com.gogidix.shared.courier.ecommerce.domain.repository;

import java.util.Optional;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;

public interface EcommerceTrackingRepository {
    EcommerceDeliveryTracking save(EcommerceDeliveryTracking tracking);
    Optional<EcommerceDeliveryTracking> findById(String id);
    Optional<EcommerceDeliveryTracking> findByOrderId(String orderId);
    Optional<EcommerceDeliveryTracking> findBySubOrderId(String subOrderId);
    Optional<EcommerceDeliveryTracking> findByTrackingId(String trackingId);
}
