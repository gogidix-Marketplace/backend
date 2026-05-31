package com.gogidix.shared.courier.ecommerce.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceTrackingRepository;
import com.gogidix.shared.courier.ecommerce.infrastructure.persistence.repository.MongoEcommerceTrackingRepository;

@Component
public class EcommerceTrackingRepositoryAdapter implements EcommerceTrackingRepository {

    private final MongoEcommerceTrackingRepository mongoRepository;

    public EcommerceTrackingRepositoryAdapter(MongoEcommerceTrackingRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public EcommerceDeliveryTracking save(EcommerceDeliveryTracking tracking) {
        return mongoRepository.save(tracking);
    }

    @Override
    public Optional<EcommerceDeliveryTracking> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<EcommerceDeliveryTracking> findByOrderId(String orderId) {
        return mongoRepository.findByOrderId(orderId);
    }

    @Override
    public Optional<EcommerceDeliveryTracking> findBySubOrderId(String subOrderId) {
        return mongoRepository.findBySubOrderId(subOrderId);
    }

    @Override
    public Optional<EcommerceDeliveryTracking> findByTrackingId(String trackingId) {
        return mongoRepository.findByTrackingId(trackingId);
    }
}
