package com.gogidix.ecommerce.paymentmethod.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import com.gogidix.ecommerce.paymentmethod.domain.port.out.PaymentMethodRepositoryPort;
import com.gogidix.ecommerce.paymentmethod.infrastructure.persistence.repository.PaymentMethodMongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentMethodPersistenceAdapter implements PaymentMethodRepositoryPort {

    private final PaymentMethodMongoRepository mongoRepository;

    public PaymentMethodPersistenceAdapter(PaymentMethodMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public PaymentMethod save(PaymentMethod entity) {
        return mongoRepository.save(entity);
    }

    @Override
    public Optional<PaymentMethod> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    public Page<PaymentMethod> findByTenantId(String tenantId, Pageable pageable) {
        return mongoRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }
}