package com.gogidix.ecommerce.paymentgateway.infrastructure.persistence.repository;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentGatewayMongoRepository extends MongoRepository<PaymentGateway, String> {
    List<PaymentGateway> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}
