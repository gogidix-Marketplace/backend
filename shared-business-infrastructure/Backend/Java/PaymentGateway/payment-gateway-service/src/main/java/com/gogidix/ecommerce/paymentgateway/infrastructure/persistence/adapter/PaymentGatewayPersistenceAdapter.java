package com.gogidix.ecommerce.paymentgateway.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import com.gogidix.ecommerce.paymentgateway.domain.port.out.PaymentGatewayRepositoryPort;
import com.gogidix.ecommerce.paymentgateway.infrastructure.persistence.repository.PaymentGatewayMongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentGatewayPersistenceAdapter implements PaymentGatewayRepositoryPort {

    private final PaymentGatewayMongoRepository mongoRepository;

    public PaymentGatewayPersistenceAdapter(PaymentGatewayMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public PaymentGateway save(PaymentGateway entity) {
        return mongoRepository.save(entity);
    }

    @Override
    public Optional<PaymentGateway> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    public Page<PaymentGateway> findByTenantId(String tenantId, Pageable pageable) {
        return mongoRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }
}