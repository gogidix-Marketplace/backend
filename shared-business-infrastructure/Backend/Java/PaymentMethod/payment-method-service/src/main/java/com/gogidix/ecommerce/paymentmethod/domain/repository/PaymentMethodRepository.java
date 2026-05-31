package com.gogidix.ecommerce.paymentmethod.domain.repository;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends MongoRepository<PaymentMethod, String> {

    Optional<PaymentMethod findByIdAndTenantId(String id, String tenantId);

    Page<PaymentMethod> findByTenantId(String tenantId, Pageable pageable);
}