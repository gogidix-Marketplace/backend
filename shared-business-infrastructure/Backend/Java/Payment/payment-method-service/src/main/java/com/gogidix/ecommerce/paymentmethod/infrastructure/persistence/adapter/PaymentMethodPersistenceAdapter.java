package com.gogidix.ecommerce.paymentmethod.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import com.gogidix.ecommerce.paymentmethod.domain.port.out.PaymentMethodRepositoryPort;
import com.gogidix.ecommerce.paymentmethod.infrastructure.persistence.repository.PaymentMethodMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentMethodPersistenceAdapter implements PaymentMethodRepositoryPort {

    private final PaymentMethodMongoRepository mongoRepository;

    public PaymentMethodPersistenceAdapter(PaymentMethodMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public PaymentMethod save(PaymentMethod entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<PaymentMethod> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<PaymentMethod> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
