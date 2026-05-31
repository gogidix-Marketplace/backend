package com.gogidix.ecommerce.paymentgateway.domain.repository;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentGatewayRepository extends MongoRepository<PaymentGateway, String> {

    Optional<PaymentGateway findByIdAndTenantId(String id, String tenantId);

    Page<PaymentGateway> findByTenantId(String tenantId, Pageable pageable);
}