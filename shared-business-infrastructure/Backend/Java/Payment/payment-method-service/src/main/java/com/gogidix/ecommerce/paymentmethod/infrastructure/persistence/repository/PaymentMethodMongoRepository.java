package com.gogidix.ecommerce.paymentmethod.infrastructure.persistence.repository;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentMethodMongoRepository extends MongoRepository<PaymentMethod, String> {
    List<PaymentMethod> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
