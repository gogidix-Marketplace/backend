package com.gogidix.ecommerce.payment.infrastructure.persistence.repository;

import com.gogidix.ecommerce.payment.domain.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentMongoRepository extends MongoRepository<Payment, String> {
    List<Payment> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
