package com.gogidix.shared.courier.ecommerce.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceAssignmentRepository;
import com.gogidix.shared.courier.ecommerce.infrastructure.persistence.repository.MongoEcommerceAssignmentRepository;

@Component
public class EcommerceAssignmentRepositoryAdapter implements EcommerceAssignmentRepository {

    private final MongoEcommerceAssignmentRepository mongoRepository;

    public EcommerceAssignmentRepositoryAdapter(MongoEcommerceAssignmentRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public EcommerceCourierAssignment save(EcommerceCourierAssignment assignment) {
        return mongoRepository.save(assignment);
    }

    @Override
    public Optional<EcommerceCourierAssignment> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<EcommerceCourierAssignment> findByOrderIdAndSubOrderId(String orderId, String subOrderId) {
        return mongoRepository.findByOrderIdAndSubOrderId(orderId, subOrderId);
    }

    @Override
    public Optional<EcommerceCourierAssignment> findByTrackingId(String trackingId) {
        return mongoRepository.findByTrackingId(trackingId);
    }

    @Override
    public List<EcommerceCourierAssignment> findByOrderId(String orderId) {
        return mongoRepository.findByOrderId(orderId);
    }

    @Override
    public List<EcommerceCourierAssignment> findByPickupZoneIdAndStatus(String zoneId, EcommerceCourierAssignment.AssignmentStatus status) {
        return mongoRepository.findByPickupZoneIdAndStatus(zoneId, status.name());
    }

    @Override
    public List<EcommerceCourierAssignment> findByDeliveryZoneIdAndStatus(String zoneId, EcommerceCourierAssignment.AssignmentStatus status) {
        return mongoRepository.findByDeliveryZoneIdAndStatus(zoneId, status.name());
    }
}
