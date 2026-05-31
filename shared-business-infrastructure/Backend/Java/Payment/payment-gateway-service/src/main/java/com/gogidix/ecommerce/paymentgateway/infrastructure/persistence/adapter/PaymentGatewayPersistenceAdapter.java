package com.gogidix.ecommerce.paymentgateway.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import com.gogidix.ecommerce.paymentgateway.domain.port.out.PaymentGatewayRepositoryPort;
import com.gogidix.ecommerce.paymentgateway.infrastructure.persistence.repository.PaymentGatewayMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentGatewayPersistenceAdapter implements PaymentGatewayRepositoryPort {

    private final PaymentGatewayMongoRepository mongoRepository;

    public PaymentGatewayPersistenceAdapter(PaymentGatewayMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public PaymentGateway save(PaymentGateway entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<PaymentGateway> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<PaymentGateway> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}
