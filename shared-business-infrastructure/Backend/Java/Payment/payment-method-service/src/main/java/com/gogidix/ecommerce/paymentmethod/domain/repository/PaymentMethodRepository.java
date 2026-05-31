package com.gogidix.ecommerce.paymentmethod.domain.repository;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends MongoRepository<PaymentMethod, String> {
    List<PaymentMethod> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
