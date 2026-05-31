package com.gogidix.ecommerce.payment.domain.repository;

import com.gogidix.ecommerce.payment.domain.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
