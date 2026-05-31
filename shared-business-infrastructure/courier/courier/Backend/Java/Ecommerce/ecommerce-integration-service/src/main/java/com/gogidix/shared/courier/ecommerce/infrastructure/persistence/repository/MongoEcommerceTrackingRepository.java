package com.gogidix.shared.courier.ecommerce.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;

@Repository
public interface MongoEcommerceTrackingRepository extends MongoRepository<EcommerceDeliveryTracking, String> {

    Optional<EcommerceDeliveryTracking> findByOrderId(String orderId);

    Optional<EcommerceDeliveryTracking> findBySubOrderId(String subOrderId);

    Optional<EcommerceDeliveryTracking> findByTrackingId(String trackingId);
}
