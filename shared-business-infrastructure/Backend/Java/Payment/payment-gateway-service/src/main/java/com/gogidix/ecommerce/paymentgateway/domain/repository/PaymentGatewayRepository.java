package com.gogidix.ecommerce.paymentgateway.domain.repository;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentGatewayRepository extends MongoRepository<PaymentGateway, String> {
    List<PaymentGateway> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
