package com.gogidix.ecommerce.payment.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.payment.domain.model.Payment;
import com.gogidix.ecommerce.payment.domain.port.out.PaymentRepositoryPort;
import com.gogidix.ecommerce.payment.infrastructure.persistence.repository.PaymentMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentPersistenceAdapter implements PaymentRepositoryPort {

    private final PaymentMongoRepository mongoRepository;

    public PaymentPersistenceAdapter(PaymentMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Payment save(Payment entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Payment> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Payment> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
