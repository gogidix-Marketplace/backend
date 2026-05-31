package com.gogidix.ecommerce.paymentmethod.infrastructure.persistence.repository;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentMethodMongoRepository extends MongoRepository<PaymentMethod, String> {

    Optional<PaymentMethod> findByIdAndTenantId(String id, String tenantId);

    Page<PaymentMethod> findByTenantId(String tenantId, Pageable pageable);
}